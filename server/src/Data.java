package server;

import server.interfaces.Chat;
import server.interfaces.ChatMessage;
import server.interfaces.User;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Data {
    public static HashMap<String, Chat> chats = new HashMap<>();
    public static Set<User> users = new HashSet<>();
    public static void addToMessages(String chatName, ChatMessage cm) {
        chats.get(chatName).chatMessages.add(cm);
    }

    public static void updateUserSocket(String username, Socket s) {
        for(User u: users){
            if(u.username.equals(username)) {
                u.socket = s;
            }
        }
    }

    public static Boolean isUserJoinedInGroup(String chatname, String username) {
        return chats.get(chatname).members.containsKey(username);
    }
}
