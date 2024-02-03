import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


public class UserMgmt extends JFrame {

    private credUtil CredsObj = new credUtil();
    private OTPUtil OTPobj = new OTPUtil();

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
        JButton resendOtpBtn = new JButton("Resend OTP");
        resendOtpBtn.setEnabled(false);
        resendOtpBtn.setToolTipText("You can resend OTP once every 30 seconds");
        JPanel OTPFieldGroup = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton sendOTPBtn = new JButton("Send OTP");
        JButton cancelBtn = new JButton("Cancel");
        // After OTP sent components
        JButton verifyOTPBtn = new JButton("Verify");
        JButton recancelBtn = new JButton("Cancel");
        // After OTP verification components
        JLabel newPassLabel = new JLabel("Set New Password: ");
        JPasswordField newPassField = new JPasswordField();
        JButton resetPassBtn = new JButton("Reset Password");

        OTPFieldGroup.add(OTPField);
        OTPFieldGroup.add(resendOtpBtn);

        forgotForm.add(forgotEmailLabel);
        forgotForm.add(forgotEmailField);
        forgotForm.add(OTPLabel);
        forgotForm.add(OTPFieldGroup);
        forgotForm.add(sendOTPBtn);
        forgotForm.add(cancelBtn);

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

                                JOptionPane.showMessageDialog(mainPanel, "Account added!", "Info", JOptionPane.INFORMATION_MESSAGE);

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

        backLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.remove(registerForm);
                mainPanel.add(loginContainer);
                revalidate();
                repaint();
            }
        });


        forgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainPanel.remove(loginContainer);
                mainPanel.add(forgotForm);
                revalidate();
                repaint();
            }
        });

        Timer timer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resendOtpBtn.setEnabled(true);
            }
        });
        timer.setRepeats(false);

        sendOTPBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = forgotEmailField.getText();
                
                if(CredsObj.isMailExists(mail)) {
                    forgotEmailField.setEnabled(false);
                    OTPobj.sendOTP(mail, false);

                    JOptionPane.showMessageDialog(mainPanel, "Mail sent", "Info", JOptionPane.INFORMATION_MESSAGE);

                    OTPField.setEnabled(true);
                    timer.start();

                    forgotForm.remove(sendOTPBtn);
                    forgotForm.remove(cancelBtn);
                    forgotForm.add(verifyOTPBtn);
                    forgotForm.add(recancelBtn);
                    revalidate();
                    repaint();
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Email does not exists in our database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OTPField.setText("");
                OTPField.setEnabled(false);
                resendOtpBtn.setEnabled(false);
                forgotEmailField.setText("");
                mainPanel.remove(forgotForm);
                mainPanel.add(loginContainer);
                revalidate();
                repaint();
            }
        });

        resendOtpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = forgotEmailField.getText();
                
                if(CredsObj.isMailExists(mail)) {
                    OTPobj.sendOTP(mail, true);

                    JOptionPane.showMessageDialog(mainPanel, "Mail sent", "Info", JOptionPane.INFORMATION_MESSAGE);

                    resendOtpBtn.setEnabled(false);
                    timer.start();
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Email does not exists in our database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        verifyOTPBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String OTPfromField = OTPField.getText();
                String OTPfromObj = OTPobj.getOTP();

                if(OTPfromField.equals(OTPfromObj)) {
                    timer.stop();

                    OTPField.setText("");
                    OTPField.setEnabled(false);
                    resendOtpBtn.setEnabled(false);

                    forgotForm.remove(verifyOTPBtn);
                    forgotForm.remove(recancelBtn);

                    forgotForm.add(newPassLabel);
                    forgotForm.add(newPassField);
                    forgotForm.add(resetPassBtn);
                    forgotForm.add(recancelBtn);

                    revalidate();
                    repaint();
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Incorrect OTP, try again!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });

        resetPassBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = forgotEmailField.getText();
                String pass = String.valueOf(newPassField.getPassword());

                if(!pass.isBlank()) {
                    try {
                        String hashedPass = getSHA256(pass);
                        CredsObj.resetPassWord(mail, hashedPass);
    
                        JOptionPane.showMessageDialog(mainPanel, "Password reset successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
    
                        recancelBtn.doClick();
                    }
                    catch(NoSuchAlgorithmException | UnsupportedEncodingException err) {
                        err.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Password field cannot be blank", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });

        recancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forgotEmailField.setEnabled(true);
                OTPField.setText("");
                OTPField.setEnabled(false);
                resendOtpBtn.setEnabled(false);
                timer.stop();
                forgotEmailField.setText("");
                newPassField.setText("");

                forgotForm.remove(newPassLabel);
                forgotForm.remove(newPassField);
                forgotForm.remove(verifyOTPBtn);
                forgotForm.remove(resendOtpBtn);
                forgotForm.remove(resetPassBtn);
                forgotForm.remove(recancelBtn);

                forgotForm.add(sendOTPBtn);
                forgotForm.add(cancelBtn);

                mainPanel.remove(forgotForm);
                mainPanel.add(loginContainer);
                revalidate();
                repaint();
            }
        });

        add(mainPanel);
    }

    public static void main(String[] args) {
        UserMgmt u = new UserMgmt();
        u.setVisible(true);
    }
}
