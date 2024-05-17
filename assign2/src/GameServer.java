import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class GameServer {
    private int port;
    private List<ClientHandler> clients = new ArrayList<>();
    private UserData userData = new UserData();
    private ExecutorService pool = Executors.newCachedThreadPool();
    private volatile int currentTurn = 0; 
    private final ReentrantLock turnLock = new ReentrantLock(); 
    private final Condition turnCondition = turnLock.newCondition(); 
    private final CountDownLatch latch = new CountDownLatch(3); 
    private volatile boolean gameStarted = false; 
    private final ReentrantLock clientLock = new ReentrantLock();

    public GameServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientLock.lock();
                try {
                    clients.add(clientHandler);
                } finally {
                    clientLock.unlock();
                }
                pool.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameServer server = new GameServer(12345);
        server.start();
    }

    private void broadcast(String message) {
        clientLock.lock();
        try {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        } finally {
            clientLock.unlock();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        private int points = 0; 

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Welcome! Register or Login?");
                String action = in.readLine();
                String username = "";
                String password = "";

                if ("register".equalsIgnoreCase(action)) {
                    out.println("Enter username:");
                    username = in.readLine();
                    out.println("Enter password:");
                    password = in.readLine();
                    if (userData.registerUser(username, password)) {
                        out.println("Registration successful");
                    } else {
                        out.println("Username already exists");
                    }
                } else if ("login".equalsIgnoreCase(action)) {
                    out.println("Enter username:");
                    username = in.readLine();
                    out.println("Enter password:");
                    password = in.readLine();
                } else {
                    out.println("Invalid action. Closing connection.");
                    return;
                }

                if (userData.authenticateUser(username, password)) {
                    this.username = username;
                    out.println("Login successful");
                    latch.countDown();

                    int playersConnected = 3 - (int) latch.getCount();
                    broadcast(playersConnected + "/3 players connected to start the game");
                    out.println("Waiting for other players to join...");

                    latch.await();

                    synchronized (GameServer.class) {
                        if (!gameStarted) {
                            gameStarted = true;
                            broadcast("All players connected. The game is starting!");
                        }
                    }

                    gameLoop();
                } else {
                    out.println("Invalid credentials");
                }

                out.println("Goodbye!");

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void gameLoop() throws IOException {
            out.println("Game started");
            int round = 1;

            while (round <= 3) {
                turnLock.lock();
                try {
                    while (clients.indexOf(this) != currentTurn) {
                        try {
                            turnCondition.await();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    out.println("Roll the Dice");
                    String diceRollStr = in.readLine();
                    int diceRoll = Integer.parseInt(diceRollStr);
                    points += diceRoll;
                    System.out.println("Player " + username + " rolled a " + diceRoll);
                    out.println("Roll received: " + diceRoll);

                    currentTurn = (currentTurn + 1) % clients.size();

                    if (currentTurn == 0) {
                        round++;
                    }

                    if (round > 3) {
                        break;
                    }

                    turnCondition.signalAll();
                } finally {
                    turnLock.unlock();
                }
            }

            announceResults();
        }

        private void announceResults() throws IOException {
            broadcast("Game Results:");
            clientLock.lock();
            try {
                for (ClientHandler client : clients) {
                    broadcast(client.username + " made " + client.points + " points");
                }

                ClientHandler winner = clients.get(0);
                for (ClientHandler client : clients) {
                    if (client.points > winner.points) {
                        winner = client;
                    }
                }
                broadcast("The winner is " + winner.username + " with " + winner.points + " points!");

                disconnectAllClients();
            } finally {
                clientLock.unlock();
            }
        }

        private void sendMessage(String message) {
            out.println(message);
        }

        private void disconnectAllClients() {
            clientLock.lock();
            try {
                for (ClientHandler client : clients) {
                    try {
                        client.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                clientLock.unlock();
            }
        }
    }
}
