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

	private Map<String, PublicKey> publicKeys = new HashMap<>();

	private KeyTool() {
	}

	public static KeyTool getInstance() {
		return INSTANCE;
	}

	public void loadKey(String alias, String file) {
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(file)
            );
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			PublicKey publicKey = cf.generateCertificate(in).getPublicKey();
			publicKeys.put(alias, publicKey);

		} catch (CertificateException | IOException e) {
            e.printStackTrace();
        }

    }

	public byte[] encrypt(String alias, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKeys.get(alias));

			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException ignore)
        {

        }

        return null;
	}
}
