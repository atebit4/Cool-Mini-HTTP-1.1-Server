import java.io.IOException;
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
            
            //each client gets its own thread so multi client threading :D
            //calls passess socket to client class 
            Thread clientThread = new Thread(new Client(clientSocket));
            
            clientThread.start();
        }
    }

}
