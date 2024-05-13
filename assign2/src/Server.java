import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int SERVER_PORT = 8888;
    private Map<String, String> userCredentials = new HashMap<>();
    private List<Socket> connectedPlayers = Collections.synchronizedList(new ArrayList<>());
    private ExecutorService gameService = Executors.newCachedThreadPool();
    private final int requiredPlayers = 3;  // Set to 3 players as required for the game

    public Server() {
        loadUserCredentials();
    }

    private void loadUserCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    userCredentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user credentials: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private synchronized void registerUser(String username, String password) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_credentials.txt", true))) {
            writer.write(username + ":" + password + "\n");
        }
        userCredentials.put(username, password);  // Update the map after registering new user
    }

    private boolean authenticate(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    public static void main(String[] args) {
        Server server = new Server();
        System.out.println("Server is listening on port " + SERVER_PORT);
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                server.handleClient(socket);
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleClient(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        
        String request = reader.readLine();
        String[] requestParts = request.split(" ");
        String[] credentials = requestParts[1].split(":");
        if (requestParts[0].equals("register") && credentials.length == 2) {
            // Handle registration
            registerUser(credentials[0], credentials[1]);
            writer.println("Registration Successful, Authenticated");
            connectedPlayers.add(socket);
            checkAndStartGame();
        } else if (requestParts[0].equals("login") && authenticate(credentials[0], credentials[1])) {
            writer.println("Authenticated");
            connectedPlayers.add(socket);
            checkAndStartGame();
        } else {
            writer.println("Authentication Failed");
            socket.close();
        }
    }

    private void checkAndStartGame() {
        if (connectedPlayers.size() >= requiredPlayers) {
            List<Socket> playersForGame = new ArrayList<>(connectedPlayers.subList(0, requiredPlayers));
            connectedPlayers.subList(0, requiredPlayers).clear();
            Game game = new Game(requiredPlayers, playersForGame);
            gameService.submit(game::start);
        }
    }
}
