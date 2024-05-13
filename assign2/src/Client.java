import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Register or Login? (register/login):");
        String action = scanner.nextLine().trim().toLowerCase();
        
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            
            // Send credentials
            writer.println(action + " " + username + ":" + password);
            
            // Read server response
            String serverResponse = reader.readLine();
            System.out.println(serverResponse); // Print server response
            
            if (serverResponse.contains("Authenticated")) {
                // Handle game interactions
                boolean playing = true;
                while (playing) {
                    String serverCommand = reader.readLine();
                    System.out.println(serverCommand); // Print server game command
                    
                    switch (serverCommand) {
                        case "continue":
                            // Send a random valid number or implement specific game logic here
                            int numberToSend = (int) (Math.random() * 10) + 1; // Sends a number between 1 and 10
                            writer.println(numberToSend);
                            break;
                        case "stop":
                            playing = false;
                            break;
                    }
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
