import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


class EmailUtil {
    private String SMTPHostName;
    private String SMTPport;
    private String SMTPUserName;
    private String SMTPpass;

    public EmailUtil() {
        File f = new File("../src/.env");

        try {
            String content = new String(Files.readAllBytes(f.toPath()));
            String lines[] = content.split("\\r?\\n");

            SMTPHostName = lines[0].split("=")[1];
            SMTPport = lines[1].split("=")[1];
            SMTPUserName = lines[2].split("=")[1];
            SMTPpass = lines[3].split("=")[1];
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }

    public void sendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);

            // Set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("CU2024.30@example.com", "ShambaC"));

            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Msg is ready");
            Transport.send(msg);

            System.out.println("mail sent");
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }

    public void TLSMail(String toEmail, String subject, String body) {
        String fromEmail = SMTPUserName;
        String password = SMTPpass;
        
        Properties prop = new Properties();
        prop.put("mail.smtp.host", SMTPHostName);
        prop.put("mail.smtp.port", SMTPport);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getInstance(prop, auth);

        sendEmail(session, toEmail, subject, body);
    }
}

class credUtil {
    private Map<String, String> creds = new HashMap<String, String>();

    public credUtil() {
        File f = new File("../db/Creds.db");

        if(f.exists() && !f.isDirectory()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                creds = (HashMap) in.readObject();
                in.close();
            }
            catch(IOException | ClassNotFoundException err) {
                err.printStackTrace();
            }
        }
    }

    /**
     * Checks the database to find if the mail exists
     * @param mail The email address to check against the database
     * @return  Whether the mail is found in DB
     */
    public boolean isMailExists(String mail) {
        return creds.containsKey(mail);
    }

    /**
     * Checks if the mail and password match the entry in the database
     * @param mail  The mail to check
     * @param pass  The pass associated with the mail
     * @return  The result of matching
     */
    public boolean isMailPassMatch(String mail, String pass) {
        String obtainPass = creds.get(mail);

        return obtainPass.equals(pass);
    }

    private void saveCredsToFile() {
        try {
            FileOutputStream fout = new FileOutputStream("../db/Creds.db");
            ObjectOutputStream out = new ObjectOutputStream(fout);

            out.writeObject(creds);
            out.flush();
            out.close();
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }

    /**
     * Adds a new user to the DB
     * @param mail  Mail to add to the DB
     * @param pass  Password for the user to be associated with the mail
     */
    public void addEntry(String mail, String pass) {
        creds.put(mail, pass);
        saveCredsToFile();
    }

    /**
     * Resets the password with a new one for the specified mail
     * @param mail  The mail for which the password will be changed
     * @param pass  The new password
     */
    public void resetPassWord(String mail, String pass) {
        creds.replace(mail, pass);
        saveCredsToFile();
    }
}

public class UserMgmt extends JFrame {

    private credUtil CredsObj = new credUtil();
    private EmailUtil mailUtil = new EmailUtil();

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
        
        JPanel loginContainer = new JPanel(new GridLayout(0, 1));

        loginContainer.add(loginForm);
        loginContainer.add(forgot);

        mainPanel.add(loginContainer);

        JPanel registerForm = new JPanel(new GridLayout(0, 2, 30, 15));
        registerForm.setPreferredSize(new Dimension(500, 250));

        Border regFormBorder = BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Registration Form"), new EmptyBorder(30, 30, 30, 30));
        registerForm.setBorder(regFormBorder);

        JLabel regUserLabel = new JLabel("Email: ");
        JTextField regEmailField = new JTextField();
        JLabel regPassLabel = new JLabel("Password: ");
        JPasswordField regPassField = new JPasswordField();
        JLabel regPassConfLabel = new JLabel("Confirm Password: ");
        JPasswordField regPassConfField = new JPasswordField();
        JButton newRegBtn = new JButton("Register");
        JButton backLoginBtn = new JButton("Login");

        registerForm.add(regUserLabel);
        registerForm.add(regEmailField);
        registerForm.add(regPassLabel);
        registerForm.add(regPassField);
        registerForm.add(regPassConfLabel);
        registerForm.add(regPassConfField);
        registerForm.add(newRegBtn);
        registerForm.add(backLoginBtn);

        JPanel forgotForm = new JPanel(new GridLayout(0, 2, 30, 15));
        forgotForm.setPreferredSize(new Dimension(600, 250));

        Border forgotBorder = BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Forgot Password"), new EmptyBorder(30, 30, 30, 30));
        forgotForm.setBorder(forgotBorder);

        JLabel forgotEmailLabel = new JLabel("Email: ");
        JTextField forgotEmailField = new JTextField();
        JLabel OTPLabel = new JLabel("OTP: ");
        JTextField OTPField = new JTextField();
        OTPField.setEnabled(false);
        JButton sendOTPBtn = new JButton("Send OTP");
        JButton cancelBtn = new JButton("Cancel");
        // After OTP verification components
        JLabel newPassLabel = new JLabel("Set New Password: ");
        JPasswordField newPassField = new JPasswordField();
        JButton resetPassBtn = new JButton("Reset Password");

        forgotForm.add(forgotEmailLabel);
        forgotForm.add(forgotEmailField);
        forgotForm.add(OTPLabel);
        forgotForm.add(OTPField);
        forgotForm.add(sendOTPBtn);
        forgotForm.add(cancelBtn);


        ActionListener backToLoginAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.remove(registerForm);
                mainPanel.remove(forgotForm);
                mainPanel.add(loginContainer);
                revalidate();
                repaint();
            }
        };

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = loginMailField.getText();
                String pass = String.valueOf(loginPassField.getPassword());

                if(!mail.isBlank() && !pass.isBlank()) {
                    if(CredsObj.isMailExists(mail)) {
                        try {
                            String hashedPassword = getSHA256(pass);
                            if(CredsObj.isMailPassMatch(mail, hashedPassword)) {
                                EmpMgmt E = new EmpMgmt();
                                E.setVisible(true);
                                setVisible(false);
                            }
                            else {
                                JOptionPane.showMessageDialog(mainPanel, "Mail Password do not match", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        catch(NoSuchAlgorithmException | UnsupportedEncodingException err) {
                            err.printStackTrace();
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(mainPanel, "Email does not exists in our database", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Please fill up all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginMailField.setText("");
                loginPassField.setText("");

                mainPanel.remove(loginContainer);
                mainPanel.add(registerForm);
                revalidate();
                repaint();
            }
        });

        newRegBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = regEmailField.getText();
                String pass = String.valueOf(regPassField.getPassword());
                String passCnf = String.valueOf(regPassConfField.getPassword());

                if(!email.isBlank() && !pass.isBlank() && !passCnf.isBlank()) {
                    if(CredsObj.isMailExists(email)) {
                        JOptionPane.showMessageDialog(mainPanel, "Email already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        if(pass.equals(passCnf)) {
                            try {
                                String hashedPass = getSHA256(passCnf);
                                CredsObj.addEntry(email, hashedPass);

                                regEmailField.setText("");
                                regPassField.setText("");
                                regPassConfField.setText("");
                            }
                            catch(NoSuchAlgorithmException | UnsupportedEncodingException err) {
                                err.printStackTrace();
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(mainPanel, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Please fill up all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        backLoginBtn.addActionListener(backToLoginAction);


        forgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainPanel.remove(loginContainer);
                mainPanel.add(forgotForm);
                revalidate();
                repaint();
            }
        });

        sendOTPBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = forgotEmailField.getText();
                
                if(CredsObj.isMailExists(mail)) {
                    Random random = new Random();
                    String otp = String.format("%04d", random.nextInt(10000));

                    String mailContent = "Hello User, \nYour OTP to reset password is: " + otp;
                    String mailSubject = "OTP for CU EMS";

                    mailUtil.TLSMail(mail, mailSubject, mailContent);

                    JOptionPane.showMessageDialog(mainPanel, "Mail sent", "Info", JOptionPane.INFORMATION_MESSAGE);

                    OTPField.setEnabled(true);
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Email does not exists in our database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelBtn.addActionListener(backToLoginAction);

        add(mainPanel);
    }

    public static void main(String[] args) {
        UserMgmt u = new UserMgmt();
        u.setVisible(true);
    }
}
