package co.greetings.model.util;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class StringUtils {
    private StringUtils() {}
    
    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isAlphanumeric(String s) {
        return s != null && s.matches("^[a-zA-Z0-9]*$");
    }

    public static boolean isEmail(String email) {
        return email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }

    public static byte[] encryptText(String password) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec("8333247922".toCharArray(), "f4abe9ef83362".getBytes(), 65536, 256);
        SecretKey originalKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES_CIPHER_ALGORITHM"); 
        var ivParameterSpec = new IvParameterSpec(new byte[] { 124, 100, 68, -57, -79, 32, 3, 22, -8, -127, 77, 60, 16, 90, -11, 92}); 
        cipher.init(Cipher.ENCRYPT_MODE, originalKey, ivParameterSpec);
        return cipher.doFinal(password.getBytes());
    }
}
