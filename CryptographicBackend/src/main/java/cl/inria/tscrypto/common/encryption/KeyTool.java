package cl.inria.tscrypto.common.encryption;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class KeyTool {

	private static KeyTool INSTANCE = new KeyTool();

	private Map<String, PublicKey> publicKeys = new HashMap<String, PublicKey>();

	private KeyTool() {
	}

	public static KeyTool getInstance() {
		return INSTANCE;
	}

	public void loadKey(String alias, String file) {
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(file));
			CertificateFactory cf = CertificateFactory.getInstance("X.509");

			PublicKey publicKey = cf.generateCertificate(in).getPublicKey();
			
			publicKeys.put(alias, publicKey);

		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public byte[] encrypt(String alias, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKeys.get(alias));

			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
