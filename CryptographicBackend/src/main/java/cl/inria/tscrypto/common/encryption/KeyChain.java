package cl.inria.tscrypto.common.encryption;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import cl.inria.tscrypto.common.utils.TSLogger;

public class KeyChain {

	private static KeyChain INSTANCE = new KeyChain();
	
	private KeyStore keyStore;
	private ProtectionParameter protection;

	private Map<String, PrivateKey> privateKeys = new HashMap<String, PrivateKey>();

	private KeyChain() {
	}
	
	public static KeyChain getInstance() {
		return INSTANCE;
	}
	
	public void loadKeyStoreFile(String filename, char[] password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(new FileInputStream(filename), password);

		protection = new KeyStore.PasswordProtection(password);
	}

	public byte[] decrypt(String rsaKeyAlias, String encryptedData) {
		byte[] decrypted = null;

		try {
			Cipher decipher = Cipher.getInstance("RSA");
			decipher.init(Cipher.DECRYPT_MODE, getPrivateKey(rsaKeyAlias));
			decrypted = decipher.doFinal(Base64.decodeBase64(encryptedData));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
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
		return decrypted;
	}

	private PrivateKey getPrivateKey(String rsaKeyAlias)
			throws NoSuchAlgorithmException, UnrecoverableEntryException,
			KeyStoreException {

		PrivateKey privateKey = privateKeys.get(rsaKeyAlias);

		if (null == privateKey) {
			privateKey = ((PrivateKeyEntry) keyStore
					.getEntry(rsaKeyAlias, protection)).getPrivateKey();
			privateKeys.put(rsaKeyAlias, privateKey);
		}
		return privateKey;
	}

	public static void consoleSetup(String filename) {
		Console console = System.console();
		if (null == console)
			System.exit(-1);

		char[] password = console.readPassword("Enter keystore password: ");
		KeyChain keyChain = KeyChain.getInstance();
		try {
			keyChain.loadKeyStoreFile(filename, password);
		} catch (Exception e) {
			TSLogger.node.error("Could not load KeyStore");
			System.exit(-1);
			e.printStackTrace();
		}
		TSLogger.node.info("Successfuly loaded KeyStore");
	}
}
