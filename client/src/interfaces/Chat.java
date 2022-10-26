package client.interfaces;

import java.util.ArrayList;

public class Chat {
    public String name;
    public boolean amIJoined;
    public ArrayList<ChatMessage> chatMessages;


    public Chat (String name, ArrayList<ChatMessage> chatMessages,boolean amIjoined) {
        this.name = name;
        this.chatMessages = chatMessages;
        this.amIJoined = amIjoined;
    }
}
