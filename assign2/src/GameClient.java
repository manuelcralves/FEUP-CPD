import java.io.*;
import java.net.*;

public class GameClient {
    private String host;
    private int port;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println(in.readLine());
            String action = console.readLine();
            out.println(action);
            if ("register".equalsIgnoreCase(action) || "login".equalsIgnoreCase(action)) {
                System.out.println(in.readLine());
                out.println(console.readLine());
                System.out.println(in.readLine());
                out.println(console.readLine());
            }

            while (true) {
                String serverMessage = in.readLine();
                if (serverMessage == null) {
                    System.out.println("Disconnected from server.");
                    break;
                }
                System.out.println("Server: " + serverMessage);
                if (serverMessage.equals("Roll the Dice")) {
                    System.out.println("Press Enter to roll the dice or type 'disconnect' to exit...");
                    String input = console.readLine();
                    if ("disconnect".equalsIgnoreCase(input)) {
                        out.println("disconnect");
                        break;
                    } else {
                        int diceRoll = (int) (Math.random() * 6) + 1; 
                        out.println(diceRoll);
                        System.out.println("You rolled a " + diceRoll);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameClient client = new GameClient("localhost", 12345);
        client.start();
    }
}
