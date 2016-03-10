package tanks.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityUtil {
    public static String getPasswordDigest(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (password == null) {
            return null;
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(digest.digest());
    }
}
