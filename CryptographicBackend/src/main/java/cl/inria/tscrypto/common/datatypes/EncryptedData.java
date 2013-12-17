package cl.inria.tscrypto.common.datatypes;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cl.inria.tscrypto.common.encryption.KeyChain;
import cl.inria.tscrypto.common.encryption.KeyTool;
import cl.inria.tscrypto.common.utils.Util;

public class EncryptedData extends JsonFormat {
	private static final int KEYSIZE_AES = 128;

	/** data encrypted using AES */
	public String encryptedData;
	/** AES key encrypted with RSA's public key specified by the alias */
	public String encryptedKey;

	/** alias of the RSA's public key used to encrypt/decrypt the AES key */
	public String rsaKeyAlias;

	/**
	 * Constructor for encrypted data
	 * @param rsaKeyAlias alias of the RSA's public key of the receiver
	 * @param blob data to be encrypted
	 */
	public EncryptedData(String rsaKeyAlias, byte[] blob) {
		this.rsaKeyAlias = rsaKeyAlias;
		addData(blob);
	}

	/**
	 * Returns a decrypted version of the included data
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public byte[] decrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] plainAESKey = KeyChain.getInstance().decrypt(rsaKeyAlias, encryptedKey);
		SecretKeySpec skeySpec = new SecretKeySpec(plainAESKey, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return cipher.doFinal(Base64.decodeBase64(encryptedData));
	}
	
	/**
	 * Creates an object of the Class specified
	 * @param <T> Type of object to create
	 * @param json encrypted data
	 * @param theClass type of the object to create
	 * @return Newly object of type T (theClass)
	 * @throws JsonSyntaxException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createObject(String json, Class<?> theClass) throws JsonSyntaxException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Gson gson = Util.GsonFactory(true);
		EncryptedData encryptedData = gson.fromJson(json, EncryptedData.class);
		return  (T) gson.fromJson(new String(encryptedData.decrypt()), theClass);
	}

	final private byte[] encryptAES(SecretKeySpec skeySpec, byte[] data) {

		byte[] encrypted = null;

		try {
			// Instantiate the cipher
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			encrypted = cipher.doFinal(data);
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

		return encrypted;
	}

	final private static SecretKeySpec generateAESKey() {
		SecretKeySpec skeySpec = null;
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(KEYSIZE_AES);

			// Generate the secret key specs.
			SecretKey secretKey = keyGen.generateKey();

			skeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return skeySpec;
	}

	final private byte[] encrypt(byte[] data) {
		return KeyTool.getInstance().encrypt(this.rsaKeyAlias, data);
	}

	private void addData(byte[] blob) {
		SecretKeySpec skeySpec = generateAESKey();
		encryptedData = new String(Base64.encodeBase64(encryptAES(
				skeySpec, blob)));
		encryptedKey = new String(Base64.encodeBase64(encrypt(skeySpec
				.getEncoded())));
	}
	
}
