import javax.swing.text.AttributeSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.math.BigInteger;

import java.net.ServerSocket;
import java.net.Socket;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Friends {
    private String name;
    private String ip;
    private int port;

    Friends(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }
    public String getIP() {
        return ip;
    }
    public int getPort() {
        return port;
    }
    public String getIPPort() {
        return ip + ":" + port;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean equals(Friends f) {
        return (this.ip.equalsIgnoreCase(f.getIP()) && this.port == f.getPort());
    }
}

class ChatContent {
    private String content;
    private StyledDocument contentStyle;

    ChatContent(String content, StyledDocument contentStyle) {
        this.content = content;
        this.contentStyle = contentStyle;
    }

    public String getContent() {
        return content;
    }
    public StyledDocument getContentStyle() {
        return contentStyle;
    }
}

public class client extends JFrame implements Runnable {
    List<Friends> selectedFriends = new ArrayList<Friends>();

    ServerSocket rootSocket;
    Socket connectionSocket;

    private int ownPort = 9090;

    JTextPane chatHistory;
    ChatContent defaultChatContent;

    JList friendsList;
    DefaultListModel<Friends> model = new DefaultListModel<Friends>();

    Map<String, ChatContent> friendsChatHistory = new HashMap<String, ChatContent>();
    Friends lastSelectedFriend = null;
    
    public client() {
        setTitle("IRC Java");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        init();
    }
    public client(int ownPort) {
        this.ownPort = ownPort;

        setTitle("IRC Java");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        init();
    }

    private void appendToPane(JTextPane tp, String msg, Color C) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, C);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aSet, false);
        tp.replaceSelection(msg);
    }

    private String getSHA256(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(str.getBytes("UTF-8"));

            BigInteger num = new BigInteger(1, hash);
            StringBuilder hexStr = new StringBuilder(num.toString(16));

            while(hexStr.length() < 32) {
                hexStr.insert(0, '0');
            }

            return hexStr.toString();
    }

    private void init() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(4, 4, 4, 4);

        JPanel mainPanel = new JPanel(gb);
        mainPanel.setBackground(new Color(55, 55, 55));

        Font font = new Font("Calibri", Font.PLAIN, 30);

        gbc.weightx = 12;
        JLabel chatTitle = new JLabel("Chat History");
        chatTitle.setForeground(Color.WHITE);
        chatTitle.setFont(font);
        gb.setConstraints(chatTitle, gbc);

        gbc.weightx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JLabel listTitle = new JLabel("Friends");
        listTitle.setForeground(Color.WHITE);
        listTitle.setFont(font);
        gb.setConstraints(listTitle, gbc);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 4, true);

        gbc.weightx = 12;
        gbc.weighty = 12;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        chatHistory = new JTextPane();
        chatHistory.setFont(new Font("Calibri", Font.ITALIC, 22));
        appendToPane(chatHistory, "Your chat begins here //\n", new Color(93, 173, 92));
        chatHistory.setEditable(false);
        chatHistory.setFont(new Font("Calibri", Font.PLAIN, 22));
        StyledDocument defaultDoc = chatHistory.getStyledDocument();
        String defaultDocContent = chatHistory.getText();
        defaultChatContent = new ChatContent(defaultDocContent, defaultDoc);


        JScrollPane chatScroll = new JScrollPane(chatHistory);
        chatScroll.setBorder(border);
        gb.setConstraints(chatScroll, gbc);

        gbc.weightx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        friendsList = new JList(model);
        friendsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        friendsList.setFont(new Font("Calibri", Font.PLAIN, 22));
        JScrollPane frndScroll = new JScrollPane(friendsList);
        frndScroll.setBorder(border);
        gb.setConstraints(frndScroll, gbc);

        friendsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    selectedFriends = friendsList.getSelectedValuesList();

                    if(lastSelectedFriend != null) {
                        friendsChatHistory.replace(getSHA256(lastSelectedFriend.getIPPort()), new ChatContent(chatHistory.getText(), chatHistory.getStyledDocument()));
                    }
                    lastSelectedFriend = (Friends) friendsList.getSelectedValue();
                    ChatContent tempContent = friendsChatHistory.get(getSHA256(lastSelectedFriend.getIPPort()));
                    chatHistory.setText(tempContent.getContent());
                    chatHistory.setStyledDocument(tempContent.getContentStyle());
                }
                catch(NoSuchAlgorithmException err) {}
                catch(UnsupportedEncodingException err) {}
            }
        });

        gbc.weightx = 14;
        gbc.weighty = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JTextField messageField = new JTextField();
        gb.setConstraints(messageField, gbc);

        gbc.weightx = 1;
        // gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(font);
        gb.setConstraints(nameLabel, gbc);

        gbc.weightx = 4;
        JTextField nameField = new JTextField();
        gb.setConstraints(nameField, gbc);

        gbc.weightx = 1;
        JLabel IPLabel = new JLabel("IP");
        IPLabel.setForeground(Color.WHITE);
        IPLabel.setFont(font);
        gb.setConstraints(IPLabel, gbc);

        gbc.weightx = 4;
        JTextField IPfield = new JTextField();
        gb.setConstraints(IPfield, gbc);

        gbc.weightx = 1;
        JLabel PortLabel = new JLabel("Port");
        PortLabel.setForeground(Color.WHITE);
        PortLabel.setFont(font);
        gb.setConstraints(PortLabel, gbc);

        JTextField Portfield = new JTextField();
        gb.setConstraints(Portfield, gbc);

        JButton addButton = new JButton("Add");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gb.setConstraints(addButton, gbc);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fName = nameField.getText();
                    String ip = IPfield.getText();
                    if(ip.equalsIgnoreCase("localhost")) {
                        ip = "127.0.0.1";
                    }
                    int port = Integer.parseInt(Portfield.getText());
                    Friends f = new Friends(fName, ip, port);

                    boolean isFriendExist = false;
                    for(int i = 0; i < model.getSize(); i++) {
                        Friends fList = model.getElementAt(i);

                        if(f.equals(fList)) {
                            JOptionPane.showMessageDialog(mainPanel, "Friend already exists, renamed.", "Info", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Renamed " + fList.getName() + " to " + fName);
                            fList.setName(fName);
                            isFriendExist = true;
                            friendsList.repaint();
                            friendsList.setSelectedIndex(i);
                            break;
                        }
                    }

                    if(!isFriendExist) {
                        model.addElement(f);
                        friendsChatHistory.put(getSHA256(f.getIPPort()), defaultChatContent);
                        friendsList.setSelectedIndex(model.getSize() - 1);
                        System.out.println("Added "+ fName + " " + ip + ":" + port);
                    }

                    nameField.setText("");
                    IPfield.setText("");
                    Portfield.setText("");
                }
                catch(NoSuchAlgorithmException err) {}
                catch(UnsupportedEncodingException err) {}
            }         
            
        });

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JButton sendButton = new JButton("Send");
        gb.setConstraints(sendButton, gbc);

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(model.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "No friends, make some first.", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                String msg = messageField.getText();

                for(Friends f : selectedFriends) {
                    try {
                        sendMessage(msg, f);
                        messageField.setText("");
                    }
                    catch(Exception err) {
                        err.printStackTrace();
                    }
                }
            }
        });


        mainPanel.add(chatTitle);
        mainPanel.add(listTitle);
        mainPanel.add(chatScroll);
        mainPanel.add(frndScroll);
        mainPanel.add(messageField);

        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        mainPanel.add(IPLabel);
        mainPanel.add(IPfield);
        mainPanel.add(PortLabel);
        mainPanel.add(Portfield);

        mainPanel.add(addButton);
        mainPanel.add(sendButton);

        add(mainPanel);
    }

    private void sendMessage(String msg, Friends f) throws Exception {
        Socket clientSocket = new Socket(f.getIP(), f.getPort());
        OutputStream outToFriend = clientSocket.getOutputStream();
        String ipDets = clientSocket.getLocalAddress().toString() + ":" + ownPort + "\n";
        outToFriend.write(ipDets.getBytes());
        msg += "\n";
        outToFriend.write(msg.getBytes());

        for(int i = 0; i < model.getSize(); i++) {
            Friends fList = model.getElementAt(i);
            if(fList.equals(f)) {
                friendsList.setSelectedIndex(i);
                break;
            }
        }

        chatHistory.setEditable(true);
        appendToPane(chatHistory, "You: " + msg, new Color(227, 50, 68));
        chatHistory.setEditable(false);
    }

    @Override
    public void run() {
        try {
            rootSocket = new ServerSocket(ownPort);

            while(true) {
                connectionSocket = rootSocket.accept();

                BufferedReader inFromFriend = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                String ipDets = inFromFriend.readLine();
                String remoteIP = ipDets.split(":")[0].replace("/", "");
                int remoteServerPort = Integer.parseInt(ipDets.split(":")[1]);

                String friendName = "";
                boolean isExistingFriend = false;
                for(int i = 0; i < model.getSize(); i++) {
                    Friends fList = model.getElementAt(i);
                    Friends fTemp = new Friends("Anon", remoteIP, remoteServerPort);

                    if(fList.equals(fTemp)) {
                        isExistingFriend = true;
                        friendName = fList.getName();
                        friendsList.setSelectedIndex(i);
                        break;
                    }
                }

                if(!isExistingFriend) {
                    model.addElement(new Friends("Anon" + model.getSize(), remoteIP, remoteServerPort));
                    friendName = "Anon" + (model.getSize() - 1);
                    friendsChatHistory.put(getSHA256(remoteIP + ":" + remoteServerPort), defaultChatContent);
                    friendsList.setSelectedIndex(model.getSize() - 1);
                    System.out.println("Added " + "Anon" + (model.getSize() - 1) + " " + remoteIP + ":" + remoteServerPort);
                }

                String msg = inFromFriend.readLine();
                connectionSocket.close();
                chatHistory.setEditable(true);
                appendToPane(chatHistory, friendName + ": " + msg + "\n", new Color(68, 83, 199));
                chatHistory.setEditable(false);
                continue;
            }

        }
        catch(IOException err) {
            err.printStackTrace();
        }
        catch(NoSuchAlgorithmException err) {}
    }

    public int getClientPort() {
        return ownPort;
    }

    public static void main(String[] args) {
        client C = null;
        if(args.length != 0) {
            int portArg = Integer.parseInt(args[0]);
            C = new client(portArg);
        }
        else {
            C = new client();
        }
        System.out.println("Opening server at port " + C.getClientPort());
        C.setVisible(true);

        Thread t1 = new Thread(C);
        t1.start();
    }
}
