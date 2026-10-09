
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
                        
                        boolean forbiddenattempt = request.getPath().contains("../") || request.getPath().contains("..\\");
                        //forbidden 
                        // stuff like ../ or /passwd should not be allowed
                        if(forbiddenattempt || !file_path.startsWith(Path.of("server_root").toAbsolutePath().normalize())) {
                            status = HttpStatus.FORBIDDEN;
                            System.out.println("403: Forbidden");
                            content = "".getBytes();
                        } else if(!Files.isRegularFile(file_path)) { //404 not found, looking for a file that does not exist
                            status = HttpStatus.NOT_FOUND;
                            System.out.println("404: Not Found");
                            content = "".getBytes();
                       
                        } else { //else its OK
                            status = HttpStatus.OK;
                            System.out.println("200: OK");

                            //last modified date 
                            Instant ILastModified = Files.getLastModifiedTime(file_path).toInstant().truncatedTo(ChronoUnit.SECONDS);
                            LastModified = HttpDateFormat.format(ILastModified);
                            content = Files.readAllBytes(file_path);
                            //check for 304 not modified
                            String if304 = request.getHeader("If-Modified-Since");
                            if(if304!=null && request.getMethod().equals("GET")) {
                                try{
                                    Instant reqtime = ZonedDateTime.parse(if304, HttpDateFormat).toInstant().truncatedTo(ChronoUnit.SECONDS);
                                    if(!ILastModified.isAfter(reqtime)) {
                                        status = HttpStatus.NOT_MODIFIED;
                                        System.out.println("304: Not Modified");
                                        content = "".getBytes();
                                    }
                                } catch (Exception e) {
                                    //invalid leave as 200 OK
                                }
                            } else { //if no if-modified-since header, just read the file
                                content = Files.readAllBytes(file_path);
                            }
                            //content = Files.readAllBytes(file_path);
                        }
                        //content = Files.readAllBytes(file_path);
                        
                    } // Handle other request types
                    else {
                        status = HttpStatus.NOT_IMPLEMENTED;
                        System.out.println("501: Not Implemented");
                        content = "".getBytes();
                    }

                    //headers for response
                    String headers = request.getVersion() + " " + status.getCode() + " " + status.getMessage() + "\r\n" + "Content-Type: text/html\r\n" +  "Server: The Cool Server" + "\r\n" + "Date: " + datetime.format(HttpDateFormat) + "\r\n";

                    //on OK 200 add last modified date to header
                    if(status == HttpStatus.OK) {
                        headers += "Last-Modified: " + LastModified + "\r\n" ;
                    } //on 304 not modified do not add content length to header
                    if(status != HttpStatus.NOT_MODIFIED) {
                        headers += "Content-Length: " + content.length + "\r\n";
                    }
                    
                    //keep-alive connection header to allow multiple requests on the same connection
                    headers += """
                               Connection: keep-alive\r
                               \r
                               """;
                    //send response
                    socketOutput.write(headers.getBytes());
                
                    //send content if GET request
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
                    
                    //flush output
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