import java.util.Random;

public class OTPUtil {
    private String OTP;
    private EmailUtil mailUtil = new EmailUtil();

    public OTPUtil() {
        Random random = new Random();
        OTP = String.format("%04d", random.nextInt(10000));
    }

    public String getOTP() {
        return OTP;
    }

    public void sendOTP(String mail, boolean isResend) {
        if(isResend) {
            Random random = new Random();
            OTP = String.format("%04d", random.nextInt(10000));
        }

        String mailContent = "Hello User, \nYour OTP to reset password is: " + OTP;
        String mailSubject = "OTP for CU EMS";

        mailUtil.TLSMail(mail, mailSubject, mailContent);
    }
}
