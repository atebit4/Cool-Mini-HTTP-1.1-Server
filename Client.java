
import java.io.*;
import java.net.Socket;

    class Client implements Runnable {
        private Socket clientSocket1;

        public Client(Socket socket) {
            this.clientSocket1 = socket;
        }

        @Override
        public void run() {
            try {
                // ###### Fill in Start ######
                String line = null;
                
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
                while( (line=socketInput.readLine()) != null && !line.equals("")) { //need to change this to have multiple requests on same connection
                    System.out.println(line);
                }

                System.out.println("\n");
                PrintWriter socketOutput = new PrintWriter(clientSocket1.getOutputStream(), true);
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
                    clientSocket1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
    }}