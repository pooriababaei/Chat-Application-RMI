package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Chat {
    public static int port = 8080;

    private Server_Chat() {
//        try {
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }


    public static void main(String[] args) {
        ServerSocket ss;

        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = ss.accept(); //Listening on and accepting new clients.
                System.out.println("New client connected" + clientSocket.getInetAddress().getHostAddress());
                new ReadWrite(clientSocket).start(); // Create new thread to read from and write to the client.

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

