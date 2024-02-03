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

    // Get the credentials util
    private credUtil CredsObj = new credUtil();
    // Get the OTP util
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

        // Login form
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

        // New user registration form
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

        // Forgot password page
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

        // Login logic
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the mail and password from the form fields
                String mail = loginMailField.getText();
                String pass = String.valueOf(loginPassField.getPassword());

                // If the inputs are valid
                if(!mail.isBlank() && !pass.isBlank()) {
                    // Check the database for the mail to see if it exists
                    if(CredsObj.isMailExists(mail)) {
                        try {
                            // hash the password with SHA256
                            String hashedPassword = getSHA256(pass);
                            // Check if the email provided matches the corresponding password from the database
                            if(CredsObj.isMailPassMatch(mail, hashedPassword)) {
                                // If it matches, launch the Employee Management UI
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
        // Button to show the registration form
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset the login form fields
                loginMailField.setText("");
                loginPassField.setText("");

                // Remove the login form
                mainPanel.remove(loginContainer);
                // Show the registration form
                mainPanel.add(registerForm);
                // Update the UI
                revalidate();
                repaint();
            }
        });

        // Button to register a new user
        newRegBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the registration form data
                String email = regEmailField.getText();
                String pass = String.valueOf(regPassField.getPassword());
                String passCnf = String.valueOf(regPassConfField.getPassword());

                // Check if all the inputs were provided and valid
                if(!email.isBlank() && !pass.isBlank() && !passCnf.isBlank()) {
                    // Check if email already exists
                    if(CredsObj.isMailExists(email)) {
                        // Then show, new registration not possible
                        JOptionPane.showMessageDialog(mainPanel, "Email already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        // Check if both the password fields are consistent
                        if(pass.equals(passCnf)) {
                            try {
                                // Hash the password with SHA256
                                String hashedPass = getSHA256(passCnf);
                                // Add the user to the DB
                                CredsObj.addEntry(email, hashedPass);

                                // Show confirmation dialog
                                JOptionPane.showMessageDialog(mainPanel, "Account added!", "Info", JOptionPane.INFORMATION_MESSAGE);

                                // Reset the registration form
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

        // Button to go back to login window from the registration window
        backLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove the registration form
                mainPanel.remove(registerForm);
                // Add the login form
                mainPanel.add(loginContainer);
                // Update the UI
                revalidate();
                repaint();
            }
        });

        // Listen for clicks on the forgot password label
        forgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // show the password reset screen
                mainPanel.remove(loginContainer);
                mainPanel.add(forgotForm);
                revalidate();
                repaint();
            }
        });

        // Create a 30 second timer for the resend OTP button
        Timer timer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Enable the resend OTP button every 30 second
                resendOtpBtn.setEnabled(true);
            }
        });
        // Do not loop the timer
        // The timer is only required every time the resend button is disabled
        timer.setRepeats(false);

        // Button to send OTP
        sendOTPBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the mail from the form field
                String mail = forgotEmailField.getText();
                
                // Check if the mail exists in the DB, only then password can be reset
                if(CredsObj.isMailExists(mail)) {
                    // Disable the email field after it has been verified to be existing
                    // This is done so that when the password reset part comes up, user cannot change the email to change password for a different user
                    forgotEmailField.setEnabled(false);
                    // Use the OTP utility class to send the OTP to the user via their mail
                    OTPobj.sendOTP(mail, false);

                    // Show confirmation dialog
                    JOptionPane.showMessageDialog(mainPanel, "Mail sent", "Info", JOptionPane.INFORMATION_MESSAGE);

                    // Enable the OTP input field
                    OTPField.setEnabled(true);
                    // Start the timer to resend OTP
                    timer.start();

                    // Remove the Send OTP and Cancel button
                    forgotForm.remove(sendOTPBtn);
                    forgotForm.remove(cancelBtn);
                    // Add the verify OTP button and a new cancel button specifically meant for later stages
                    forgotForm.add(verifyOTPBtn);
                    forgotForm.add(recancelBtn);
                    // Update the UI
                    revalidate();
                    repaint();
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Email does not exists in our database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Default cancel button for the password reset page
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset the OTP text field and disable it
                OTPField.setText("");
                OTPField.setEnabled(false);
                // Disable the resend OTP button as well
                resendOtpBtn.setEnabled(false);
                // Reset the user email field
                forgotEmailField.setText("");
                // Remove the password reset form
                mainPanel.remove(forgotForm);
                // Add the login form window
                mainPanel.add(loginContainer);
                // Update the UI
                revalidate();
                repaint();
            }
        });

        // The resend OTP button
        resendOtpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get mail from the field
                //(The field was disabled to disallow editing)
                String mail = forgotEmailField.getText();
                
                // Check if the mail exists
                // This check is rather redundant
                if(CredsObj.isMailExists(mail)) {
                    // Use the OTP utility to send an OTP to the user mail
                    OTPobj.sendOTP(mail, true);

                    // Show confirmation dialog
                    JOptionPane.showMessageDialog(mainPanel, "Mail sent", "Info", JOptionPane.INFORMATION_MESSAGE);

                    // Disable the resend button
                    resendOtpBtn.setEnabled(false);
                    // Start the timer to reenable the resend button
                    timer.start();
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Email does not exists in our database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Verify OTP button
        verifyOTPBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the user given OTP from the form field
                String OTPfromField = OTPField.getText();
                // Get the generated OTP from the utility
                String OTPfromObj = OTPobj.getOTP();

                // Check for the input and generated OTPs' equality
                if(OTPfromField.equals(OTPfromObj)) {
                    // Stop the timer as it is not required anymore
                    timer.stop();

                    // Reset the OTP field and disable the field
                    OTPField.setText("");
                    OTPField.setEnabled(false);
                    resendOtpBtn.setEnabled(false);

                    // Remove verify buttons
                    forgotForm.remove(verifyOTPBtn);
                    forgotForm.remove(recancelBtn);

                    // Add the password reset fields
                    forgotForm.add(newPassLabel);
                    forgotForm.add(newPassField);
                    forgotForm.add(resetPassBtn);
                    forgotForm.add(recancelBtn);

                    // Update the UI
                    revalidate();
                    repaint();
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Incorrect OTP, try again!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });

        // Reset password button
        resetPassBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the mail and the new password from the form
                String mail = forgotEmailField.getText();
                String pass = String.valueOf(newPassField.getPassword());

                // Check only for validity of password as the mail field is locked
                if(!pass.isBlank()) {
                    try {
                        // hash the password with SHA256
                        String hashedPass = getSHA256(pass);
                        // Reset the password using the utility
                        CredsObj.resetPassWord(mail, hashedPass);
    
                        JOptionPane.showMessageDialog(mainPanel, "Password reset successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
    
                        // Force to the login form window by clicking on the cancel button programatically
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

        // New cancel button
        recancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset fields
                forgotEmailField.setEnabled(true);
                OTPField.setText("");
                OTPField.setEnabled(false);
                resendOtpBtn.setEnabled(false);
                timer.stop();
                forgotEmailField.setText("");
                newPassField.setText("");

                // Remove the components
                forgotForm.remove(newPassLabel);
                forgotForm.remove(newPassField);
                forgotForm.remove(verifyOTPBtn);
                forgotForm.remove(resendOtpBtn);
                forgotForm.remove(resetPassBtn);
                forgotForm.remove(recancelBtn);

                // Add back the previously removed buttons
                forgotForm.add(sendOTPBtn);
                forgotForm.add(cancelBtn);

                // Remove and the add the required components
                mainPanel.remove(forgotForm);
                mainPanel.add(loginContainer);
                // Update UI
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
