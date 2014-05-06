/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.tscrypto.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cl.niclabs.tscrypto.common.datatypes.BigIntegerBase64TypeAdapter;
import cl.niclabs.tscrypto.common.messages.EncryptedData;
import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyShares;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;
import cl.niclabs.tscrypto.common.encryption.KeyTool;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.messages.TSMessageParser;

/**
 * Generic utility methods
 * 
 * @author vramiro
 * @author acansado
 */
public class Util {
	
	private Util() {
		// this class should not be instantiated
	}

    /**
	 * Function that reads a file from disk to a String
	 * 
	 * @param filename
	 *            to open
	 * @return String with the file contents
	 * @throws IOException
	 */
	public static String readFile(String filename) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String line;
		StringBuffer buffer = new StringBuffer();

		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}

		return buffer.toString();
	}

    /**
	 * Helper to create a List with one element
	 * 
	 * @param e
	 *            element to add in the list
	 * @return a list with one element (given by parameter)
	 */
	public static <T> List<T> newSingletonList(T e) {
		List<T> l = new ArrayList<T>();
		l.add(e);

		return l;
	}

	public static Properties loadTrimedProperties(String filename)
			throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(filename));

		trimProperties(properties);

		return properties;
	}

	public static void trimProperties(Properties props) {

		for (String name : props.stringPropertyNames()) {
			props.setProperty(name, props.getProperty(name).trim());
		}

	}

    /*
	public static KeyMetaInfo loadKeyMetaInfoFromFile(String filename)
			throws IOException {
		String json = readFile(filename);

		TSLogger.node.debug("read file " + filename);

		Gson gson = GsonFactory(true);
		KeyMetaInfo key = gson.fromJson(json, KeyMetaInfo.class);

		TSLogger.node.debug(key.toJson(true));

		return key;
	}


	public static TSPublicKey loadPublicKeyFromFile(String filename)
			throws IOException {
		String json = readFile(filename);

		TSLogger.node.info("read file " + filename);

		Gson gson = GsonFactory(true);
		TSPublicKey key = gson.fromJson(json, TSPublicKey.class);

		TSLogger.node.info(key.toJson(true));

		return key;
	}

	public static KeyShares loadKeySharesFromFile(String filename,
			KeyMetaInfo keyMetaInfo, int id) throws IOException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		String json = readFile(filename);

		TSLogger.node.debug("read file " + filename);

//		Gson gson = GsonFactory(true);
//
//		EncryptedData encryptedData = gson.fromJson(json, EncryptedData.class);
//
//		String jsonshares = new String(encryptedData.decrypt());
//
//		System.out.println("---------- KEYSHARE ----------");
//		System.out.println(jsonshares);
//		System.out.println("---------- KEYSHARE ----------");
//
//		KeyShares keyShares = gson.fromJson(jsonshares, KeyShares.class);

		KeyShares keyShares = EncryptedData.<KeyShares>createObject(json, KeyShares.class);
		
		TSLogger.node.debug(keyShares.toJson(true));

		return keyShares;
	}
	*(
	
	public static KeyInfo loadKeyFromFile(String string) throws IOException {
		// TODO file extensions should not be hardcoded
		KeyMetaInfo keyMetaInfo = Util.loadKeyMetaInfoFromFile(string
				+ ".metainfo");
		TSPublicKey publicKey = Util.loadPublicKeyFromFile(string
				+ ".publickey");

		return new KeyInfo(keyMetaInfo, publicKey);
	}

	public static KeyInfo loadKeyFromFile(String string, int id)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		KeyInfo keyInfo = loadKeyFromFile(string);

		// TODO file extensions should not be hardcoded
		String filename = string + ".keyshare-" + id;
		KeyShares keyShares = Util.loadKeySharesFromFile(filename,
				keyInfo.getKeyMetaInfo(), id);

		keyInfo.setKeyShares(keyShares);

		return keyInfo;
	}

	public static void saveStringToFile(String data, String filename)
			throws IOException {
		FileWriter fstream = new FileWriter(filename);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(data);
		out.close();
	}

	public static void saveKeyToFile(KeyInfo key, String filename,
			String[] rsaKeys) throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		KeyMetaInfo keyMetaInfo = key.getKeyMetaInfo();
		TSPublicKey publicKey = key.getPublicKey();
		KeyShares keyShares = key.getKeyShares();

		// key filename is prefixed by its alias UUID
		filename += keyMetaInfo.getAlias();
		
		// TODO file extensions should not be hardcoded
		saveStringToFile(keyMetaInfo.toJson(true), filename + ".metainfo");
		saveStringToFile(publicKey.toJson(true), filename + ".publickey");
		savePublicKeyToFile(publicKey, filename + ".pem");

		for (int id = 0; id < keyMetaInfo.getL(); id++) {
			KeyShares partial = new KeyShares(keyShares, id);
			String keyAlias = "node" + id;

			saveStringToFile(encryptKeyShare(partial, keyAlias, rsaKeys[id]),
					filename
					+ ".keyshare-" + id);
		}
	}

	private static String encryptKeyShare(KeyShares partial, String keyAlias,
			String privateKeyFilename) {
		KeyTool pkTool = KeyTool.getInstance();
		pkTool.loadKey(keyAlias, privateKeyFilename);

		EncryptedData encryptedData = new EncryptedData(keyAlias, partial
				.toJson().getBytes());
		return encryptedData.toJson(true);
	}

	private static void savePublicKeyToFile(TSPublicKey publicKey,
			String filename) throws NoSuchAlgorithmException,
			InvalidKeySpecException, IOException {
		String encoded = Base64.encodeBase64String(publicKey
				.convertoToPublicKey().getEncoded());
		FileWriter fw = new FileWriter(filename);
		fw.write("---BEGIN PUBLIC KEY---\n");
		fw.write(encoded);
		fw.write("---END PUBLIC KEY---");
		fw.close();
	}
	*/

	public static Gson GsonFactory(boolean pretty) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		if (pretty)
			gsonBuilder.setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(BigInteger.class,
				new BigIntegerBase64TypeAdapter());
		gsonBuilder.registerTypeAdapter(TSMessage.class, new TSMessageParser());
		return gsonBuilder.create();
	}

}
