package server.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat {
    public String name;
    public HashMap<String,User> members;
    public ArrayList<ChatMessage> chatMessages;

    public Chat (String name, User firstUser, ChatMessage cm) {
        this.name = name;
        this.members = new HashMap<>();
        members.put(firstUser.username,firstUser);
        this.chatMessages = new ArrayList<>();
        chatMessages.add(cm);
    }
}
