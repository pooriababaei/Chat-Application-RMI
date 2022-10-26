package client;

import client.interfaces.ChatMessage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client_Chat extends JFrame {

    private JPanel panel1;
    private JTextField newChatTF;
    private JButton createButton;
    private JList messageList;
    private JTextField messageTF;
    private JButton sendButton;
    private JList chatList;
    private JButton leaveButton;
    private JButton joinButton;

    public Client_Login dialog;

    private String url = "localhost"; // "server" for docker. "localhost" for normal run on the host
    private int port = 8080;
    Socket s;
    public static DefaultListModel<String> messages;
    public static DefaultListModel<String> chats;


    public Client_Chat() {
        this.setContentPane(panel1);
        this.setTitle("Chat App");
        this.setSize(800, 350);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.initSocket();


        chats = new DefaultListModel<String>();
        messages = new DefaultListModel<String>();
        chatList.setModel(chats);
        messageList.setModel(messages);


        showDialog();


        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!newChatTF.getText().equals("") && !ClientData.chats.containsKey(newChatTF.getText())) {
                    Write.createChat(newChatTF.getText());
                    newChatTF.setText("");
                    //  ClientData.chats.put(newChatTF.getText(),new Chat(newChatTF.getText(),null, true));
                }
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("send: " + ClientData.currentChat);
                if (ClientData.currentChat != null && !messageTF.getText().equals("")) {
                    Write.sendMessage(ClientData.currentChat, messageTF.getText());
                    messageTF.setText("");
                }


            }
        });
        chatList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (chatList.getSelectedValue() == null) return;
                String chatName = chatList.getSelectedValue().toString();
                if (chatName != null && !chatName.equals("")) {
                    ClientData.currentChat = chatName;
                    //  System.out.println("changed: " + ClientData.currentChat);

                    messages.clear();
                    if (!ClientData.isUserAMember(chatName)) {
                        joinButton.setEnabled(true);
                        leaveButton.setEnabled(false);
                        messageTF.setEnabled(false);
                        sendButton.setEnabled(false);
                    } else {
                        joinButton.setEnabled(false);
                        leaveButton.setEnabled(true);
                        messageTF.setEnabled(true);
                        sendButton.setEnabled(true);
                        Write.getChatMessages(chatName);
                    }
                }
            }
        });
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chatName = (String) chatList.getSelectedValue();
                Write.joinChat(chatName);
                ClientData.setUserStatusInGroup(chatName, true);
                joinButton.setEnabled(false);
                leaveButton.setEnabled(true);
                messageTF.setText("");
                messageTF.setEnabled(true);
                sendButton.setEnabled(true);
            }
        });
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chatName = (String) chatList.getSelectedValue();
                Write.leaveChat(chatName);
                ClientData.setUserStatusInGroup(chatName, false);
                joinButton.setEnabled(true);
                leaveButton.setEnabled(false);
                messageTF.setText("");
                messageTF.setEnabled(false);
                sendButton.setEnabled(false);

                messages.clear();
                chatList.clearSelection();
            }
        });

    }

    public void initSocket() {
        try {
            s = new Socket(url, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillChatList() {
        chats.clear();
        for (String key : ClientData.chats.keySet()) {
            chats.addElement(key);
        }
    }

    public void addChat(String chatName) {
        chats.addElement(chatName);
    }

    public void fillMessages(String chatName) {
        messages.clear();
        ArrayList<ChatMessage> cms = ClientData.chats.get(chatName).chatMessages;
        for (ChatMessage cm : cms) {
            if (cm.sender.equals(ClientData.USERNAME))
                messages.addElement("(You)" + "      " + cm.message);
            else
                messages.addElement("(" + cm.sender + ")" + "      " + cm.message);

        }
    }

    public void addMessage(String chatName, ChatMessage cm) {
        if (!chatName.equals(chatList.getSelectedValue().toString()))
            return;
        if (cm.sender.equals(ClientData.USERNAME))
            messages.addElement("(You)" + "      " + cm.message);
        else
            messages.addElement("(" + cm.sender + ")" + "      " + cm.message);
    }

    public void onLogin(String username) {
        ClientData.USERNAME = username;
        try {
            this.setTitle("Hello " + username);
            new Read(s, this).start();
            Write.outStream = new DataOutputStream(s.getOutputStream());
            Write.login(username);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        System.out.println(ClientData.USERNAME);

    }

    public void showDialog() {
        this.dialog = new Client_Login(this);
        dialog.pack();
        dialog.setVisible(true);
        // dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) this));
        // System.exit(0);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Client_Chat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(10, 10));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel2.setEnabled(false);
        panel1.add(panel2, BorderLayout.WEST);
        final JLabel label1 = new JLabel();
        label1.setText("Create new room");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel2.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer2, gbc);
        newChatTF = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(newChatTF, gbc);
        createButton = new JButton();
        createButton.setText("Create");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(createButton, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer3, gbc);
        chatList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("There is no Chatroom");
        chatList.setModel(defaultListModel1);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(chatList, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel3, gbc);
        leaveButton = new JButton();
        leaveButton.setEnabled(false);
        leaveButton.setText("Leave");
        panel3.add(leaveButton);
        joinButton = new JButton();
        joinButton.setEnabled(false);
        joinButton.setText("Join");
        panel3.add(joinButton);
        final JLabel label2 = new JLabel();
        label2.setText("Chatrooms                                    ");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label2, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        panel1.add(panel4, BorderLayout.EAST);
        final JLabel label3 = new JLabel();
        label3.setText("Messages                                                                    ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(label3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer5, gbc);
        messageList = new JList();
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        defaultListModel2.addElement("No Chat Selected");
        messageList.setModel(defaultListModel2);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel4.add(messageList, gbc);
        messageTF = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(messageTF, gbc);
        sendButton = new JButton();
        sendButton.setText("Send");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(sendButton, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer6, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
