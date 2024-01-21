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
import javax.swing.text.AttributeSet;

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

// A class to store a friends information and retrieve them
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

// Class to store chat content for individual contacts along with the style attributes
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

// Main client
// It implements runnable because it cannot extend Thread
public class client extends JFrame implements Runnable {
    // List to store the friends who have been multiselected from the friendlist
    List<Friends> selectedFriends = new ArrayList<Friends>();

    // Sockets for connection
    ServerSocket rootSocket;
    Socket connectionSocket;

    // Port to run the server socket on
    private int ownPort = 9090;

    // Pane to show the chat
    // Using JTextPane allows us to have custom styling for individual lines
    JTextPane chatHistory;
    ChatContent defaultChatContent;

    // FriendList UI and the model to store the friends
    JList friendsList;
    DefaultListModel<Friends> model = new DefaultListModel<Friends>();

    // HashMap to store chats mapped to their contacts
    Map<String, ChatContent> friendsChatHistory = new HashMap<String, ChatContent>();
    Friends lastSelectedFriend = null;
    
    // Two constructors
    // Default port and custom port
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

    /**
     * Function to add text to the JTextPane with custom colors
     * @param tp    The JTextPane to append text to
     * @param msg   The Text to append
     * @param C     Text color
     */
    private void appendToPane(JTextPane tp, String msg, Color C) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        // Set style
        AttributeSet aSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, C);

        // Get the document
        int len = tp.getDocument().getLength();
        // Go to the end of the document
        tp.setCaretPosition(len);
        // Apply the style
        tp.setCharacterAttributes(aSet, false);
        // Append the string
        tp.replaceSelection(msg);
    }

    /**
     * Method to hash a string using the SHA-256 algorithm
     * @param str String to hash
     * @return Hashed string in Hex format
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private String getSHA256(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Get the hashing function
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // Hash the string
        byte[] hash = md.digest(str.getBytes("UTF-8"));

        // Convert the hashed bytes to Hex string
        BigInteger num = new BigInteger(1, hash);
        StringBuilder hexStr = new StringBuilder(num.toString(16));

        // Pad hex string
        while(hexStr.length() < 32) {
            hexStr.insert(0, '0');
        }

        // Return the padded hashed string
        return hexStr.toString();
    }

    // Initialise the components
    private void init() {
        // Use a gridbag layout
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        // Inner padding
        gbc.insets = new Insets(4, 4, 4, 4);

        // Main Panel
        JPanel mainPanel = new JPanel(gb);
        mainPanel.setBackground(new Color(55, 55, 55));

        Font font = new Font("Calibri", Font.PLAIN, 30);

        gbc.weightx = 12;
        gbc.gridwidth = 12;
        JLabel chatTitle = new JLabel("Chat History");
        chatTitle.setForeground(Color.WHITE);
        chatTitle.setFont(font);
        gb.setConstraints(chatTitle, gbc);

        gbc.weightx = 1;
        gbc.gridwidth = 1;
        JLabel listTitle = new JLabel("Friends");
        listTitle.setForeground(Color.WHITE);
        listTitle.setFont(font);
        gb.setConstraints(listTitle, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JLabel dummyLabel = new JLabel("");
        gb.setConstraints(dummyLabel, gbc);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 4, true);

        gbc.weightx = 12;
        gbc.weighty = 12;
        gbc.gridwidth = 12;
        chatHistory = new JTextPane();
        chatHistory.setFont(new Font("Calibri", Font.ITALIC, 22));
        appendToPane(chatHistory, "Your chat begins here //\n", new Color(93, 173, 92));
        chatHistory.setEditable(false);
        chatHistory.setFont(new Font("Calibri", Font.PLAIN, 22));
        // Store the default contents of the chat panel
        StyledDocument defaultDoc = chatHistory.getStyledDocument();
        String defaultDocContent = chatHistory.getText();
        defaultChatContent = new ChatContent(defaultDocContent, defaultDoc);

        // Make chat panel scrollable
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

        // Whenever the selection in the list changes
        friendsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    // Get all the selected contacts and store them in the array list
                    selectedFriends = friendsList.getSelectedValuesList();

                    // If some chat was already open, save their chat in the hash map
                    if(lastSelectedFriend != null) {
                        friendsChatHistory.replace(getSHA256(lastSelectedFriend.getIPPort()), new ChatContent(chatHistory.getText(), chatHistory.getStyledDocument()));
                    }
                    // During multi selection, show the chat for the last selected friend
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
        messageField.setFont(new Font("Calibri", Font.PLAIN, 20));
        gb.setConstraints(messageField, gbc);

        gbc.weightx = 1;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(font);
        gb.setConstraints(nameLabel, gbc);

        gbc.weightx = 4;
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Calibri", Font.PLAIN, 20));
        gb.setConstraints(nameField, gbc);

        gbc.weightx = 1;
        JLabel IPLabel = new JLabel("IP");
        IPLabel.setForeground(Color.WHITE);
        IPLabel.setFont(font);
        gb.setConstraints(IPLabel, gbc);

        gbc.weightx = 4;
        JTextField IPfield = new JTextField();
        IPfield.setFont(new Font("Calibri", Font.PLAIN, 20));
        gb.setConstraints(IPfield, gbc);

        gbc.weightx = 1;
        JLabel PortLabel = new JLabel("Port");
        PortLabel.setForeground(Color.WHITE);
        PortLabel.setFont(font);
        gb.setConstraints(PortLabel, gbc);

        gbc.weightx = 2;
        JTextField Portfield = new JTextField();
        Portfield.setFont(new Font("Calibri", Font.PLAIN, 20));
        gb.setConstraints(Portfield, gbc);

        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        JButton addButton = new JButton("Add");
        addButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        gb.setConstraints(addButton, gbc);

        // Add friend
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get all the form data
                    String fName = nameField.getText();
                    String ip = IPfield.getText();
                    if(ip.equalsIgnoreCase("localhost")) {
                        ip = "127.0.0.1";
                    }
                    int port = Integer.parseInt(Portfield.getText());
                    Friends f = new Friends(fName, ip, port);

                    // Check if the friend that is being added already exists in the friend list
                    boolean isFriendExist = false;
                    for(int i = 0; i < model.getSize(); i++) {
                        Friends fList = model.getElementAt(i);

                        // If exists
                        if(f.equals(fList)) {
                            // Show info box
                            JOptionPane.showMessageDialog(mainPanel, "Friend already exists, renamed.", "Info", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Renamed " + fList.getName() + " to " + fName);
                            // Rename the friend in the friendlist
                            fList.setName(fName);
                            isFriendExist = true;
                            // Refresh the friendlist
                            friendsList.repaint();
                            // Select the friend
                            friendsList.setSelectedIndex(i);
                            break;
                        }
                    }

                    // If new friend
                    if(!isFriendExist) {
                        // Add to the JList model
                        model.addElement(f);
                        // Create entry in hashmap with the default chat data
                        friendsChatHistory.put(getSHA256(f.getIPPort()), defaultChatContent);
                        friendsList.setSelectedIndex(model.getSize() - 1);
                        System.out.println("Added "+ fName + " " + ip + ":" + port);
                    }

                    // Reset friend add form fields
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
        sendButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        gb.setConstraints(sendButton, gbc);

        // Called when 'return' key is pressed while focussed on the text field, in this case the message text field
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Simulates click on the button and sends actionevent to its actionlistener
                sendButton.doClick();
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if user is trying to send message when there are no friends
                if(model.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "No friends, make some first.", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                // Get the message
                String msg = messageField.getText();

                // Loop through all the selected friends
                for(Friends f : selectedFriends) {
                    // Send message and reset the message field
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

        // Add all the components to the mainpanel
        mainPanel.add(chatTitle);
        mainPanel.add(listTitle);
        mainPanel.add(dummyLabel);
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

    /**
     * Sends a message to a friend
     * @param msg The message to send
     * @param f The friend to send the message to
     * @throws Exception From socket
     */
    private void sendMessage(String msg, Friends f) throws Exception {
        // Open a socket to the friend server
        Socket clientSocket = new Socket(f.getIP(), f.getPort());
        // Open an output stream
        OutputStream outToFriend = clientSocket.getOutputStream();
        // Get own server address
        String ipDets = clientSocket.getLocalAddress().toString() + ":" + ownPort + "\n";
        // Send own server address to friend for identification
        outToFriend.write(ipDets.getBytes());
        // Send the actual message after
        msg += "\n";
        outToFriend.write(msg.getBytes());

        // Choose the friend when sending them the message
        for(int i = 0; i < model.getSize(); i++) {
            Friends fList = model.getElementAt(i);
            if(fList.equals(f)) {
                friendsList.setSelectedIndex(i);
                break;
            }
        }

        // Append text to the chat window
        chatHistory.setEditable(true);
        appendToPane(chatHistory, "You: " + msg, new Color(227, 50, 68));
        chatHistory.setEditable(false);
    }

    @Override
    public void run() {
        try {
            // Create server
            rootSocket = new ServerSocket(ownPort);

            while(true) {
                // Accept connection from another client
                connectionSocket = rootSocket.accept();

                // Read the first message from the client
                BufferedReader inFromFriend = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                String ipDets = inFromFriend.readLine();
                // Get the sender IP and Port
                String remoteIP = ipDets.split(":")[0].replace("/", "");
                int remoteServerPort = Integer.parseInt(ipDets.split(":")[1]);

                // Check if sender is already a friend
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

                // If not existing friend, add them to the friendlist
                if(!isExistingFriend) {
                    model.addElement(new Friends("Anon" + model.getSize(), remoteIP, remoteServerPort));
                    friendName = "Anon" + (model.getSize() - 1);
                    friendsChatHistory.put(getSHA256(remoteIP + ":" + remoteServerPort), defaultChatContent);
                    friendsList.setSelectedIndex(model.getSize() - 1);
                    System.out.println("Added " + "Anon" + (model.getSize() - 1) + " " + remoteIP + ":" + remoteServerPort);
                }

                // Read the incoming message
                String msg = inFromFriend.readLine();
                // Close connection
                connectionSocket.close();
                // Add the text to the chat
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

    /**
     * 
     * @return this clients port number
     */
    public int getClientPort() {
        return ownPort;
    }

    public static void main(String[] args) {
        client C = null;
        // Start client with the specified port number
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
