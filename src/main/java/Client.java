
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import request.HttpRequests;
import request.HttpStatus;
import request.RequestParser;

    class Client implements Runnable {
        private final Socket clientSocket1;

        public Client(Socket socket) {
            this.clientSocket1 = socket;
        }

        @Override
        public void run() {
            try {
                // ###### Fill in Start ######

                
            /* OLD
                //file and path handler
                Path file;
                if(request.getPath().equals("/")) {
                    file = Path.of("server_root", "index.html");
                } else {
                    file = Path.of("server_root", request.getPath());
                }
            */
                OutputStream socketOutput = clientSocket1.getOutputStream();
                BufferedInputStream socketInput = new BufferedInputStream(clientSocket1.getInputStream());
                
                //set the date for response header
                DateTimeFormatter HttpDateFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"));

                //parse the request
                while(true){
                   
                    HttpRequests request = RequestParser.parse(socketInput);
                    if (request == null) { break; }

                    HttpStatus status;
                    byte[] content;
                    String LastModified = "";
                    ZonedDateTime datetime = ZonedDateTime.now();

                    // Handle GET request
                    if(request.getMethod().equals("GET") || request.getMethod().equals("HEAD")) {
                        //status = HttpStatus.OK;
                        String file;
                        if(request.getPath().equals("/")) {
                            file = "index.html";
                        } else {
                            file = request.getPath().substring(1);
                        }
                        Path file_path = Path.of("server_root", file).toAbsolutePath().normalize();
                        
                        //forbidden 
                        // stuff like ../ or /passwd should not be allowed
                        if(!file_path.startsWith(Path.of("server_root").toAbsolutePath().normalize())) {
                            status = HttpStatus.FORBIDDEN;
                            System.out.println("403: Forbidden");
                            content = "".getBytes();
                        } else if(!Files.exists(file_path)) { //404 not found, looking for a file that does not exist
                            status = HttpStatus.NOT_FOUND;
                            System.out.println("404: Not Found");
                            content = "".getBytes();
                        } else if(request.getHeaders().containsKey("If-Modified-Since")){
                            if(LastModified.equalsIgnoreCase(request.getHeaders().get("If-Modified-Since"))){
                                status = HttpStatus.NOT_MODIFIED;
                                System.out.println("304 Not Modified");
                                content = "".getBytes();
                            }
                        } else { //else its OK
                            status = HttpStatus.OK;
                            System.out.println("200: OK");

                            //last modified date 
                            LastModified = HttpDateFormat.format(Files.getLastModifiedTime(file_path).toInstant());

                            content = Files.readAllBytes(file_path);
                        }
                        //content = Files.readAllBytes(file_path);
                        
                    } // Handle other request types
                    else {
                        status = HttpStatus.NOT_IMPLEMENTED;
                        System.out.println("501: Not Implemented");
                        content = "".getBytes();
                    }


                    String headers = request.getVersion() + " " + status.getCode() + " " + status.getMessage() + "\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + content.length + "\r\n" + "Server: The Cool Server" + "\r\n" + "Date: " + datetime.format(HttpDateFormat) + "\r\n";

                    //on OK 200 add last modified date to header
                    if(status == HttpStatus.OK) {
                        headers += "Last-Modified: " + LastModified + "\r\n" ;
                    }
                    //works
                    headers += """
                               Connection: keep-alive\r
                               \r
                               """;

                    socketOutput.write(headers.getBytes());
                
                    if (request.getMethod().equals("GET")) {
                        socketOutput.write(content);
                    }

                    //PRINT TO CONSOLE 
                    //recieve request
                    System.out.println("Received request: " + request.getMethod() + " " + request.getPath() + " " + request.getVersion() + " " + status.getCode() + " " + status.getMessage());
                    request.getHeaders().forEach((headerName, headerValue) -> System.out.printf("%s: %s%n", headerName, headerValue));
                    System.out.println("\n\n");

                    //send response
                    System.out.println("Sent response: \n" + headers);

                    System.out.println("--------------------------------------------------------\n");
                    
                    //System.out.println(socketOutput);
                    socketOutput.flush();
                    // ###### Fill in End ######
                }

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