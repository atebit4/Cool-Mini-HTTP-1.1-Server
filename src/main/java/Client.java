
import java.io.BufferedInputStream;
import java.net.Socket;

import request.HttpRequests;
import request.HttpStatus;
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
                    return;
                }

                HttpStatus status = HttpStatus.OK; // Default to OK, you can change this based logic
                String responseHeader = request.getVersion() + " " + status.getCode() + " " + status.getMessage() + "\r\n";
                
                System.out.println("Received request: " + request.getMethod() + " " + request.getPath() + " " + request.getVersion());


                //old code
                /*
                System.out.println("Received request: " + request.getMethod() + " " + request.getPath() + " " + request.getVersion());

                //need to implement GET and HEAD
                if (request.getMethod().equals("GET")) {
                    // Handle GET request
                    System.out.println("Handling GET request for: " + request.getPath());
                    // Add your GET request handling logic here
                    request.getPath();


                } else if (request.getMethod().equals("HEAD")) {
                    // Handle HEAD request
                    System.out.println("Handling HEAD request for: " + request.getPath());
                    System.out.println("Headers:");
                    request.getHeaders().forEach((name, value) -> System.out.println(name + ": " + value));
                } else {
                    System.out.println("Unsupported HTTP method: " + request.getMethod());
                }*/


                


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