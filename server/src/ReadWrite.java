package server;

import server.globals.Globals;
import server.interfaces.Chat;
import server.interfaces.ChatMessage;
import server.interfaces.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class ReadWrite extends Thread {

    Socket socket;
    String username;

    public ReadWrite(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        while (true) {
            try {

                String msg = new DataInputStream(socket.getInputStream()).readUTF();
                DataOutputStream clientOutStream = new DataOutputStream(socket.getOutputStream());
                checkAndActOnIncomingMessage(msg, clientOutStream);

            } catch (Exception ioex) {
                this.stop();
            }
        }
    }

    // detect and act on different types of incoming requests
    public void checkAndActOnIncomingMessage(String msg, DataOutputStream os) {
        try {

            if (msg.contains(Globals.LOGIN_INDICATOR)) {
            msg = msg.substring(Globals.LOGIN_INDICATOR.length());

            if(isUsernameInvalid(msg)) {
                os.writeUTF(Globals.INVALID_USERNAME);
                this.socket.close();
                this.stop();
                return; // Do not continue the rest of the login functionalities as the user has picked a repetitive username.
            }

            this.username = msg;

            if(!isUsernameRepetitive(this.username)) {
                Data.users.add(new User(this.username,this.socket));
            }
            else if(isUsernameRepetitive(this.username)){
                Data.updateUserSocket(this.username, this.socket);
            }


            String res = Globals.CHAT_LIST_INDICATOR;

            for(String c: Data.chats.keySet()) {
                res = res.concat(c + "," + Data.isUserJoinedInGroup(c,this.username).toString() + Globals.CHAT_NAME_SEPARATOR);

            }
            // System.out.println("chat list: " + Data.chats.keySet().size());
            // System.out.println("chat list: " + res);
            os.writeUTF(res);
        }

            if (msg.contains(Globals.USER_JOINED_INDICATOR)) {
                String chatName = msg.substring(Globals.USER_JOINED_INDICATOR.length());
                Data.chats.get(chatName).chatMessages.add(new ChatMessage(this.username,"Joined the chat"));
                Data.chats.get(chatName).members.put(this.username,new User(this.username,this.socket));
                Set<String> excludedUsers = new HashSet<>();
                excludedUsers.add(this.username);
                sendMessageToRoomMembers(chatName,Globals.NEW_MESSAGE_INDICATOR + chatName + Globals.CHAT_NAME_SEPARATOR + this.username + "," + "Joined the chat",excludedUsers);
                sendRoomMessagestoUser(chatName,os);
            }


            if (msg.contains(Globals.USER_LEFT_INDICATOR)) {
                String chatName = msg.substring(Globals.USER_LEFT_INDICATOR.length());
                Data.chats.get(chatName).chatMessages.add(new ChatMessage(this.username,"Left the chat"));
                Data.chats.get(chatName).members.remove(this.username);
                Set<String> excludedUsers = new HashSet<>();
                excludedUsers.add(this.username);
                sendMessageToRoomMembers(chatName,Globals.NEW_MESSAGE_INDICATOR + chatName + Globals.CHAT_NAME_SEPARATOR + this.username + "," + "Left the chat", excludedUsers);
            }


        else if (msg.contains(Globals.ROOM_MESSAGES_INDICATOR)) {
            msg = msg.substring(Globals.ROOM_MESSAGES_INDICATOR.length());
            String chatName = msg;
            sendRoomMessagestoUser(chatName,os);
        }

        else if (msg.contains(Globals.NEW_MESSAGE_INDICATOR)) {
            msg = msg.substring(Globals.NEW_MESSAGE_INDICATOR.length());
            String[] data = msg.split(Globals.CHAT_NAME_SEPARATOR);
            String chatName = data[0];
            String message = data[1];
            ChatMessage cm = new ChatMessage(this.username,message);
            Data.addToMessages(chatName, cm);
            sendMessageToRoomMembers(chatName,Globals.NEW_MESSAGE_INDICATOR + chatName + Globals.CHAT_NAME_SEPARATOR + this.username + "," + message, null);
        }

        else if (msg.contains(Globals.NEW_CHAT_INDICATOR)) {
                msg = msg.substring(Globals.NEW_CHAT_INDICATOR.length());
            if (!Data.chats.containsKey(msg)) {
                Data.chats.put(msg, new Chat(msg, new User(this.username,this.socket), new ChatMessage(this.username,"Created Chat Room")));
                noticeAllUsers(Globals.NEW_CHAT_INDICATOR + msg + Globals.CHAT_NAME_SEPARATOR + this.username);
            }
        }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessageToRoomMembers(String chatName, String msg, Set<String> excludedUsers) {
        System.out.println("send msg" + msg);
        if(excludedUsers == null) {
            for(User u: Data.chats.get(chatName).members.values()) {
                    try {
                        new DataOutputStream(u.socket.getOutputStream()).writeUTF(msg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
        }
        else {
            for(User u: Data.chats.get(chatName).members.values()) {
                if(!excludedUsers.contains(u.username)) {
                    try {
                        new DataOutputStream(u.socket.getOutputStream()).writeUTF(msg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    public void sendRoomMessagestoUser(String chatName,DataOutputStream os) {
        String res =  Globals.ROOM_MESSAGES_INDICATOR + chatName + Globals.CHAT_NAME_SEPARATOR;

        for (ChatMessage cm : Data.chats.get(chatName).chatMessages) {
            res += cm.sender + "," + cm.message + Globals.MESSAGE_SEPARATOR;
        }
        System.out.println("messages sent" + res);
        try {
            os.writeUTF(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void noticeAllUsers(String msg) {
        for(User u: Data.users) {
            try {
                new DataOutputStream(u.socket.getOutputStream()).writeUTF(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isUsernameRepetitive(String username) {
        for(User u: Data.users) {
           if(u.username.equals(username))
               return true;
        }
        return false;
    }
    public static boolean isUsernameInvalid(String username) {
            return username.contains(",") || username.contains("#");

    }
}
