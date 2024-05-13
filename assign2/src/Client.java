import java.net.*;
import java.io.*;
import java.util.Random;
 
/**
 * This program demonstrates a simple TCP/IP socket client.
 *
 * @author www.codejava.net
 */
public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;
 
    public static void main(String[] args) {
 
        String hostname = SERVER_HOST;
        int port = SERVER_PORT;
 
        try (Socket socket = new Socket(hostname, port)) {
            
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            System.out.println(reader.readLine());

            if(reader.readLine().equals("Game is starting")){

                int points = 0;
                boolean running = true;
                while(running){

                    String continue_connection = reader.readLine();

                    if ("stop".equals(continue_connection)){
                        running = false;
                    }
                    else{
                        Random rand = new Random(); 
                        int upperbound = 10;
                        int int_random = rand.nextInt(upperbound); 

                        System.out.println(int_random);

                        writer.println(int_random);
            
                        String result = reader.readLine();
            
                        System.out.println(result);
                    }
                }
            }
            else
                System.out.print("Game couldn't start");
    
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}