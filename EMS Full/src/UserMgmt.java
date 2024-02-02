import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.UnsupportedEncodingException;

import java.math.BigInteger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class UserMgmt extends JFrame {

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
    
    public UserMgmt() {
        setTitle("User Login");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        init();
    }

    private void init() {
        JPanel mainPanel = new JPanel();

        JPanel loginForm = new JPanel(new GridLayout(0, 2, 30, 15));
        loginForm.setPreferredSize(new Dimension(500, 250));

        Border formBorder = BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Login Form"), new EmptyBorder(30, 30, 30, 30));
        loginForm.setBorder(formBorder);
        
        JLabel userLabel = new JLabel("Email: ");
        JTextField loginMailField = new JTextField();
        JLabel passLabel = new JLabel("Password: ");
        JPasswordField loginPassField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginForm.add(userLabel);
        loginForm.add(loginMailField);
        loginForm.add(passLabel);
        loginForm.add(loginPassField);
        loginForm.add(loginButton);
        loginForm.add(registerButton);

        JLabel forgot = new JLabel("<html><u>Forgot Password", SwingConstants.CENTER);
        forgot.setForeground(Color.BLUE);

        forgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clicked forgor");
            }
        });
        
        JPanel loginContainer = new JPanel(new GridLayout(0, 1));

        loginContainer.add(loginForm);
        loginContainer.add(forgot);

        mainPanel.add(loginContainer);

        JPanel registerForm = new JPanel(new GridLayout(0, 2, 30, 15));
        // registerForm.setPreferredSize(new Dimension(500, 250));

        add(mainPanel);
    }

    public static void main(String[] args) {
        UserMgmt u = new UserMgmt();
        u.setVisible(true);
    }
}
