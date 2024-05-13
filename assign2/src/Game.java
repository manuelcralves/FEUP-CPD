import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {

    private List<Socket> userSockets;
    private int players;

    public Game(int players, List<Socket> userSockets) {
        this.userSockets = userSockets;
        this.players = players;
    }

    public void start() {
        System.out.println("Starting game with " + userSockets.size() + " players");

        ExecutorService executor = Executors.newFixedThreadPool(this.players);
        List<ClientHandler> clientHandlers = new ArrayList<>();

        for (int i = 0; i < this.players; i++) {
            ClientHandler handler = new ClientHandler(this.userSockets.get(i), i);
            executor.execute(handler);
            clientHandlers.add(handler);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int highestPoints = Integer.MIN_VALUE;
        int winnerId = -1;

        for (int i = 0; i < clientHandlers.size(); i++) {
            int totalPoints = clientHandlers.get(i).getTotalPoints();
            if (totalPoints > highestPoints) {
                highestPoints = totalPoints;
                winnerId = i;
            }
        }

        if (winnerId != -1)
            System.out.println("Client " + winnerId + " is the winner!");
        else 
            System.out.println("No winner found.");
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private int clientId;
        private int[] points;

        public ClientHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
            this.points = new int[3];
        }

        @Override
        public void run() {
            try (InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true)) {
                
                for (int round = 0; round < 3; round++) {
                    writer.println("continue");
                    String number = reader.readLine();
                    int int_number = Integer.parseInt(number);
                    this.points[round] = int_number;
                    System.out.println("Client " + clientId + " rolled " + int_number);

                    Thread.sleep(1000);

                    writer.println("Result");
                    
                    Thread.sleep(1000);
                }
                
                writer.println("stop");

                writer.println("Client Points");
                System.out.println("Client " + clientId + " finished with " + this.getTotalPoints() + " points.");

                
            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Server exception: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public int getTotalPoints() {
            int total = 0;
            for (int round = 0; round < 3; round++)
                total += this.points[round];

            return total;
        }
    }
}
    