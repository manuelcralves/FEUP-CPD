import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Game {

    private List<Socket> userSockets;
    private int players;

    public Game(int players, List<Socket> userSockets) {
        this.userSockets = userSockets;
        this.players = players;
    }

    public void start() {
        // Code to start the game
        System.out.println("Starting game with " + userSockets.size() + " players");

        for(int i = 0; i < this.players; i++){
            new Thread(new ClientHandler(this.userSockets.get(i), i)).start();
        }
    }


    private static class ClientHandler implements Runnable {
        private Socket socket;
        private int clientId;

        public ClientHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
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
                    System.out.println("Client " + clientId + " rolled " + int_number);
                    
                    Thread.sleep(1000);

                    writer.println("Result");
                    
                    Thread.sleep(1000);
                }
                
                writer.println("stop");

                writer.println("Client Points");

                
            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Server exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
    