import java.util.Random;

// Utility class to manage an OTP
public class OTPUtil {
    private String OTP;
    // Get the email utility class
    private EmailUtil mailUtil = new EmailUtil();

    // Constructor to initialize the OTP
    public OTPUtil() {
        Random random = new Random();
        // Get a random number from 0 to 9999
        // Pad the number to get a 4 character string
        OTP = String.format("%04d", random.nextInt(10000));
    }

    /**
     * Method to get the generated OTP
     * @return the OTP
     */
    public String getOTP() {
        return OTP;
    }

    /**
     * Method to send the OTP via mail
     * @param mail      The mail address to send the OTP to
     * @param isResend  Is the OTP being resent, then it will be regenerated
     */
    public void sendOTP(String mail, boolean isResend) {
        if(isResend) {
            Random random = new Random();
            OTP = String.format("%04d", random.nextInt(10000));
        }

        String mailContent = "Hello User, \nYour OTP to reset password is: " + OTP;
        String mailSubject = "OTP for CU EMS";

        // Send TLS mail
        mailUtil.TLSMail(mail, mailSubject, mailContent);
    }
}
