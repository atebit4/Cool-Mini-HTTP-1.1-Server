
import java.io.*;
import java.net.Socket;
import request.HttpRequests;
import request.RequestParser;

    class Client implements Runnable {
        private Socket clientSocket1;

        public Client(Socket socket) {
            this.clientSocket1 = socket;
        }

        @Override
        public void run() {
            try {
                // ###### Fill in Start ######

                BufferedInputStream socketInput = new BufferedInputStream(clientSocket1.getInputStream());
                HttpRequests request = RequestParser.parse(socketInput);
                if (request == null) {
                    System.out.println("Invalid request received.");
                    return;
                }

                System.out.println("Received request: " + request.getMethod() + " " + request.getPath() + " " + request.getVersion());

                // Print the headers
                System.out.println("Headers:");
                request.getHeaders().forEach((name, value) -> System.out.println(name + ": " + value));
                


                // ###### Fill in End ######

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
    }}