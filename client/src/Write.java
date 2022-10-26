package client;

import client.globals.Globals;

import java.io.DataOutputStream;
import java.io.IOException;

public class Write {
    public static DataOutputStream  outStream;

    public static void login(String username) {
        try {
            outStream.writeUTF(Globals.LOGIN_INDICATOR + username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createChat(String chatName) {
        try {
            outStream.writeUTF(Globals.NEW_CHAT_INDICATOR + chatName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void joinChat(String chatName) {
        try {
            outStream.writeUTF(Globals.USER_JOINED_INDICATOR + chatName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void getChatMessages(String chatName) {
        try {
            System.out.println("write   " + Globals.ROOM_MESSAGES_INDICATOR + chatName);
            outStream.writeUTF(Globals.ROOM_MESSAGES_INDICATOR + chatName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void leaveChat(String chatName) {
        try {
            outStream.writeUTF(Globals.USER_LEFT_INDICATOR + chatName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void sendMessage(String chatName, String message) {
        try {
            outStream.writeUTF(Globals.NEW_MESSAGE_INDICATOR + chatName + Globals.CHAT_NAME_SEPARATOR + message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
