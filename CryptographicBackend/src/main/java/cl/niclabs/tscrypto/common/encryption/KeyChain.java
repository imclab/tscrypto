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

package cl.niclabs.tscrypto.common.encryption;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import cl.niclabs.tscrypto.common.utils.TSLogger;

public class KeyChain {

	private static final KeyChain INSTANCE = new KeyChain();

	private KeyStore keyStore;
    private ProtectionParameter protection;

	private Map<String, PrivateKey> privateKeys = new HashMap<>();
	
	public static KeyChain getInstance() {
		return INSTANCE;
	}

    public static void init(KeyStore keyStore, ProtectionParameter protection) {
        INSTANCE.keyStore = keyStore;
        INSTANCE.protection = protection;
    }


	public byte[] decrypt(String rsaKeyAlias, String encryptedData) {
		byte[] decrypted = null;

		try {
			Cipher decipher = Cipher.getInstance("RSA");
			decipher.init(Cipher.DECRYPT_MODE, getPrivateKey(rsaKeyAlias));
			decrypted = decipher.doFinal(Base64.decodeBase64(encryptedData));
		} catch (NoSuchAlgorithmException
                | UnrecoverableEntryException
                | KeyStoreException
                | NoSuchPaddingException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException e) {
			e.printStackTrace();
		}
        return decrypted;
	}

    public byte[] sign(String rsaKeyAlias, byte[] data) throws UnrecoverableEntryException,
            NoSuchAlgorithmException, KeyStoreException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA1WithRsa");
        sign.initSign(getPrivateKey(rsaKeyAlias));
        sign.update(data);

        byte[] signature = sign.sign();
        return signature;
    }

	private PrivateKey getPrivateKey(String rsaKeyAlias)
			throws NoSuchAlgorithmException, UnrecoverableEntryException,
			KeyStoreException {

		PrivateKey privateKey = privateKeys.get(rsaKeyAlias);

		if (null == privateKey) {
			privateKey = ((PrivateKeyEntry) keyStore.getEntry(rsaKeyAlias, protection)).getPrivateKey();
			privateKeys.put(rsaKeyAlias, privateKey);
		}
		return privateKey;
	}
}
