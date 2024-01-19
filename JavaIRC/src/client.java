import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

    @Override
    public String toString() {
        return name;
    }

    public boolean equals(Friends f) {
        return (this.ip == f.getIP() && this.port == f.getPort());
    }
}

public class client extends JFrame implements Runnable {

    ServerSocket rootSocket;
    Socket connectionSocket;

    private int ownPort = 9090;

    JTextArea chatHistory;

    DefaultListModel<Friends> model = new DefaultListModel<Friends>();
    
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

    private void init() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(4, 4, 4, 4);

        // setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel(gb);
        mainPanel.setBackground(new Color(55, 55, 55));

        Font font = new Font("Calibri", Font.PLAIN, 20);

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
        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatHistory);
        chatScroll.setBorder(border);
        gb.setConstraints(chatScroll, gbc);

        gbc.weightx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JList friendsList = new JList(model);
        JScrollPane frndScroll = new JScrollPane(friendsList);
        frndScroll.setBorder(border);
        gb.setConstraints(frndScroll, gbc);

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
                String fName = nameField.getText();
                String ip = IPfield.getText();
                int port = Integer.parseInt(Portfield.getText());
                Friends f = new Friends(fName, ip, port);

                model.addElement(f);
                System.out.println("Added "+ fName + " " + ip + ":" + port);
            }         
            
        });

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JButton sendButton = new JButton("Send");
        gb.setConstraints(sendButton, gbc);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = messageField.getText();
                Friends f = model.getElementAt(0);

                try {
                    sendMessage(msg, f);
                }
                catch(Exception err) {
                    err.printStackTrace();
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
        String msg2 = msg + "\n";
        outToFriend.write(msg2.getBytes());
        chatHistory.append("You: " + msg);
    }

    @Override
    public void run() {
        try {
            rootSocket = new ServerSocket(ownPort);

            while(true) {
                connectionSocket = rootSocket.accept();

                String remoteIP = connectionSocket.getRemoteSocketAddress().toString().split(":")[0].replace("/", "");
                int remoteServerPort = Integer.parseInt(connectionSocket.getLocalSocketAddress().toString().split(":")[1]);

                String friendName = "";
                boolean isExistingFriend = false;
                for(int i = 0; i < model.getSize(); i++) {
                    Friends fList = model.getElementAt(i);
                    Friends fTemp = new Friends("Anon", remoteIP, remoteServerPort);

                    if(fList.equals(fTemp)) {
                        isExistingFriend = true;
                        friendName = fList.getName();
                        break;
                    }
                }

                if(!isExistingFriend) {
                    model.addElement(new Friends("Anon" + model.getSize(), remoteIP, remoteServerPort));
                    friendName = "Anon" + (model.getSize() - 1);
                    System.out.println("Added " + "Anon" + (model.getSize() - 1) + " " + remoteIP + ":" + remoteServerPort);
                }

                BufferedReader inFromFriend = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                String msg = inFromFriend.readLine();
                connectionSocket.close();
                chatHistory.append(friendName + ": " + msg + "\n");
                continue;
            }

        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }

    public static void main(String[] args) {
        client C = null;
        if(args.length != 0) {
            int portArg = Integer.parseInt(args[0]);
            C = new client(portArg);
            System.out.println("Opening server at port " + portArg);
        }
        else {
            C = new client();
            System.out.println("Opening server at default port");
        }
        C.setVisible(true);

        Thread t1 = new Thread(C);
        t1.start();
    }
}
