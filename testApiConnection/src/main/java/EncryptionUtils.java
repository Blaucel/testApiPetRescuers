import java.security.Key;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.SecureRandom;
import java.util.Arrays;
//import java.util.Base64;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    //Método para encriptar los datos
    // Primero la clave, ya sea la el usuario o la clave diaria; después un String con el texto a encriptar.
    public static String decrypt(String key, String encryptedData) throws Exception {
        byte[] decodedData = Base64.decodeBase64(encryptedData);
        byte[] iv = Arrays.copyOfRange(decodedData, 0, 16);
        byte[] data = Arrays.copyOfRange(decodedData, 16, decodedData.length);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedData = cipher.doFinal(data);
        return new String(decryptedData);
    }
    
    //Método para desencriptar los datos
    // Primero la clave, ya sea la el usuario o la clave diaria; después un String con el texto a desencriptar.
    public static String encrypt(String key, String data) throws Exception {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        byte[] ivAndEncryptedData = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, ivAndEncryptedData, 0, iv.length);
        System.arraycopy(encryptedData, 0, ivAndEncryptedData, iv.length, encryptedData.length);
        return Base64.encodeBase64String(ivAndEncryptedData);
    }
}

