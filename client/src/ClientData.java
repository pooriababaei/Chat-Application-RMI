package client;

import client.interfaces.Chat;
import client.interfaces.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;

//class Data {
//    // Static variable reference of single_instance
//    // of type Singleton
//    private static Data single_instance = null;
//
//    // Declaring a variable of type String
//    public  ArrayList<Chat> chats;
//
//    // Constructor
//    // Here we will be creating private constructor
//    // restricted to this class itself
//    private Data()
//    {
//        chats = new ArrayList<>();
//    }
//
//    // Static method to create instance of Singleton class
//    public static Data getInstance()
//    {
//        if (single_instance == null)
//            single_instance = new Data();
//
//        return single_instance;
//    }
//
//    public Chat findChat(String chatName) {
//        for(Chat chat : chats) {
//            if(chat.name.equals(chatName)){
//                return chat;
//            }
//        }
//        return null;
//    }
//
//    public ChatMessage addToMessages(String chatName, ChatMessage cm) {
//        findChat(chatName).chatMessages.add(cm);
//        return cm;
//    }
//
//}


public class ClientData {
    public static HashMap<String, Chat> chats = new HashMap<String, Chat>();
    public static String currentChat = null;

    public static String USERNAME;


    public static void addToMessages(String chatName, ChatMessage cm) {
        chats.get(chatName).chatMessages.add(cm);
    }

    public static boolean isUserAMember(String chatName) {
      Chat c = chats.get(chatName);
        return c != null && c.amIJoined;
    }
    public static void setUserStatusInGroup(String chatName, boolean status) {
        Chat c = chats.get(chatName);
        c.amIJoined = status;
    }

    public static void initChatsNames(ArrayList<String> chatNames) {
        for (String chatName: chatNames) {
            chats.put(chatName, null);
        }
    }

}
