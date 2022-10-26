package client;

import client.interfaces.Chat;
import client.interfaces.ChatMessage;
import client.globals.Globals;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Read extends Thread {
    DataInputStream inputStream;
    public Client_Chat cc;
    public Read(Socket s, Client_Chat cc) {
        try {
            inputStream = new DataInputStream(s.getInputStream());
            this.cc = cc;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void run() {

        while (true) {
            try {
                String msg = inputStream.readUTF();  // read message from server, this will contain uSeRLiSt#&%842=<comma seperated clientsIds>

                //This is where the input stream
                checkAndActOnIncomingMessage(msg);

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
    public void checkAndActOnIncomingMessage(String msg) {
      //  System.out.println("client: " + msg);

        if(msg.contains(Globals.INVALID_USERNAME)) {
            cc.initSocket();
            cc.showDialog();
            cc.setTitle("Hello");
        }
        else if(msg.contains(Globals.CHAT_LIST_INDICATOR)) {
            msg = msg.substring(Globals.CHAT_LIST_INDICATOR.length());
            System.out.println(msg);
            String chats[]  =  msg.split(Globals.CHAT_NAME_SEPARATOR);
            for(String s:chats) {
                String data[] = s.split(",");
                System.out.println(s);
                ClientData.chats.put(data[0], new Chat(data[0],null,Boolean.parseBoolean(data[1])));
            }
            cc.fillChatList();
        }
//        else if(msg.contains(Globals.USER_LIST_INDICATOR)) {
//            msg = msg.substring(Globals.USER_LIST_INDICATOR.length());
//
//        }
        else if(msg.contains(Globals.ROOM_MESSAGES_INDICATOR)) {
            msg = msg.substring(Globals.ROOM_MESSAGES_INDICATOR.length());
            String[] data = msg.split(Globals.CHAT_NAME_SEPARATOR);
            String chatName = data[0];
            String msgs[] = data[1].split(Globals.MESSAGE_SEPARATOR);
            System.out.println("read messages " +data[1]);
            ArrayList<ChatMessage> cms= new ArrayList<>();
            for (String m: msgs) {
                String[] parsed = m.split(",",2);
                ChatMessage cm = new ChatMessage(parsed[0],parsed[1]);
                cms.add(cm);
            }
            ClientData.chats.get(chatName).chatMessages = cms;
            cc.fillMessages(chatName);

        }
        else if(msg.contains(Globals.NEW_MESSAGE_INDICATOR)) {
            msg = msg.substring(Globals.NEW_MESSAGE_INDICATOR.length());
            String[]  data = msg.split(Globals.CHAT_NAME_SEPARATOR);
            String chatName = data[0];
            String m[] = data[1].split(",",2);
            ChatMessage cm = new ChatMessage(m[0],m[1]);
            ClientData.addToMessages(chatName,cm);
            cc.addMessage(chatName,cm);

        }
        else if(msg.contains(Globals.NEW_CHAT_INDICATOR)) {

            msg = msg.substring(Globals.NEW_CHAT_INDICATOR.length());
            String info[] = msg.split(Globals.CHAT_NAME_SEPARATOR);

           // System.out.println("info0" + info[0]);
            String chatName = info[0];
            String username = info[1];

            if(!ClientData.chats.containsKey(chatName)) {
                ClientData.chats.put(chatName,new Chat(chatName,new ArrayList<>(), username.equals(ClientData.USERNAME)));
                cc.addChat(chatName);
            }
        }

//        else if(msg.contains(Globals.USER_JOINED_INDICATOR)) {
//            msg = msg.substring(Globals.USER_JOINED_INDICATOR.length());
//        }
//        else if(msg.contains(Globals.USER_LEFT_INDICATOR)) {
//            msg = msg.substring(Globals.USER_LEFT_INDICATOR.length());
//        }

    }
}
