
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import src.main.java.request.HttpRequests;
import src.main.java.request.HttpStatus;
import src.main.java.request.RequestParser;

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
                String headers = request.getVersion() + " " + status.getCode() + " " + status.getMessage() + "\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + content.length + "\r\n" + "\r\n";
                
                //write the response header to the output stream
                socketOutput.write(headers.getBytes());
                // Handle GET request
                if(request.getMethod().equals("GET")) {
                    socketOutput.write(content);
                }
                // Handle HEAD request
                else if (request.getMethod().equals("HEAD")) {
                    System.out.println("Headers:"); // headers arehere
                }
                // Handle other request types
                else{
                    status = HttpStatus.NOT_IMPLEMENTED;
                    System.out.println("501: Not Implemented");
                }

                //PRINT TO CONSOLE 
                System.out.println("Received request: " + request.getMethod() + " " + request.getPath() + " " + request.getVersion() + " " + status.getCode() + " " + status.getMessage());
                
                System.out.println("--------------------------------------------------------\n");
                
                //System.out.println(socketOutput);
                socketOutput.flush();
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