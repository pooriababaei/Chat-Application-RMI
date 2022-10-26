package server.interfaces;

import java.net.Socket;

public class User {
   public String username;
   public Socket socket;
    public User(String username, Socket s) {
        this.username= username;
        this.socket= s;
    }
}

