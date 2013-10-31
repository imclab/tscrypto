package cl.niclabs.cb.jcrypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigesterImpl implements Digester {
    private static Digester instance = null;
    public static Digester getInstance() {
        if (instance == null) {
            synchronized (DigesterImpl.class) {
                if (instance == null) {
                    instance = new DigesterImpl();
                }
            }
        }

        return instance;
    }

    private MessageDigest messageDigest = null;

    @Override
    public void digestInit(String mechanism) throws NoSuchAlgorithmException {
        switch (mechanism) {
            case "MD5":
                messageDigest = MessageDigest.getInstance("MD5");
                break;
//            case "RIPEMD160":
//                messageDigest = MessageDigest.getInstance("RIPEMD160");
//                break;
            case "SHA-1":
                messageDigest = MessageDigest.getInstance("SHA-1");
                break;
            case "SHA-256":
                messageDigest = MessageDigest.getInstance("SHA-256");
                break;
            case "SHA-384":
                messageDigest = MessageDigest.getInstance("SHA-384");
                break;
            case "SHA-512":
                messageDigest = MessageDigest.getInstance("SHA-512");
                break;
            default:
                throw new NoSuchAlgorithmException(mechanism + " not available.");
        }
    }

    @Override
    public byte[] digest(byte[] data) throws Exception {
        if (data != null) {
            return messageDigest.digest(data);
        }

        throw new Exception("!"); // TODO!
    }

}
