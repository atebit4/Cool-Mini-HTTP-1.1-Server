import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyWebServer {
    private static ServerSocket serverSocket;
    private static int port;

    private static final String root = "server_root";

    public static void main(String[] args) throws IOException {
        port = 8888;

        serverSocket = new ServerSocket(port);
        System.out.println("The server is ready to receive on port " + port + "\n");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            
            //each client gets its own thread 
            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            
            clientThread.start();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // ###### Fill in Start ######
                String line = null;
                
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while( (line=socketInput.readLine()) != null && !line.equals("")) { //need to change this to have multiple requests on same connection
                    System.out.println(line);
                }

                System.out.println("\n");
                PrintWriter socketOutput = new PrintWriter(clientSocket.getOutputStream(), true);
                String response = "HTTP/1.1 200 OK";

                System.out.println(response);
                socketOutput.println(response);
                socketOutput.println();
                socketOutput.flush();

                // ###### Fill in End ######

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            
        }
    }
}
