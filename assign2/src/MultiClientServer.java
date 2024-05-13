import java.io.*;
import java.net.*;
import java.util.*;

public class MultiClientServer {

    private static final int SERVER_PORT = 8888;
 
    public static void main(String[] args) {
        
        while(true){

            int port = SERVER_PORT; 
            int maxClients = 3; 
            int rounds = 3; 
            int[] points = new int[maxClients]; 
            int connectedClients = 0; 
            List<Socket> userSockets = new ArrayList<Socket>();
    
            try (ServerSocket serverSocket = new ServerSocket(port)) {
    
                System.out.println("Server is listening on port " + port);
                
                while (connectedClients < maxClients) {
                    Socket socket = serverSocket.accept();
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());
                    userSockets.add(socket);
                    writer.println("Waiting for the game to begin...");
                    connectedClients++;
                }

                for(int i = 0; i < connectedClients; i++){
                    OutputStream output = userSockets.get(i).getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    writer.println("Game is starting");
                }

                Game game = new Game(connectedClients, userSockets);
                game.start();

                
            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
