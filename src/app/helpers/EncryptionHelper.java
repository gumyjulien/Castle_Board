package app.helpers;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionHelper {
    public static String encrypte(String methode, String motDePasse) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(methode);
            md.update(motDePasse.getBytes());

            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(e);
        }
    }

}
