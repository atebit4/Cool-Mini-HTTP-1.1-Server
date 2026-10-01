
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

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
                OutputStream socketOutput = clientSocket1.getOutputStream();
                BufferedInputStream socketInput = new BufferedInputStream(clientSocket1.getInputStream());
                HttpRequests request = RequestParser.parse(socketInput);
                if (request == null) { return; }

                //file and path handler
                Path file;
                if(request.getPath().equals("/")) {
                    file = Path.of("server_root", "index.html");
                } else {
                    file = Path.of("server_root", request.getPath());
                }
                byte[] content = Files.readAllBytes(file);
                
                //header stuff here
                HttpStatus status = HttpStatus.OK; // Default to OK, change with next line logic
                
                //still need to add content type and length headers, but for now just send the status code and message
                String responseHeader = request.getVersion() + " " + status.getCode() + " " + status.getMessage() + "\r\n";
                
                //write the response header to the output stream
                socketOutput.write(responseHeader.getBytes());
                // Handle GET request
                if(request.getMethod().equals("GET")) {
                    socketOutput.write(content);
                }

                //PRINT TO CONSOLE 
                System.out.println("Received request: " + request.getMethod() + " " + request.getPath() + " " + request.getVersion() + " " + status.getCode() + " " + status.getMessage());

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