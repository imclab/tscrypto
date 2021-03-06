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

package cl.niclabs.cb.jcrypto;

import cl.niclabs.cb.common.Signer;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

public class SignerImpl implements Signer {
    private String algorithm = null;
    private Cipher cipher = null;
	private Signature signature = null;
	
	@Override
    public void init(String algorithm, String keyHandler)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        PrivateKey key = MapKeyStorage.getInstance().getPrivateKey(keyHandler);

        if (key == null) {
            throw new InvalidKeyException("Llave no encontrada");
        }

        this.algorithm = algorithm;

        switch (algorithm) {
            case "RSA":
                cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                break;
            default:
                signature = Signature.getInstance(algorithm);
                signature.initSign(key);
                break;
        }

	}
	
	@Override
    public byte[] sign(byte[] data) throws Exception {
        byte[] signedData;
        switch(algorithm) {
            case "RSA":
                if (cipher == null) {
                    throw new Exception("Cipher no iniciado");
                }
                else {
                    signedData = cipher.doFinal(data);
                    cipher = null;
                    return signedData;
                }
            default:
                if (signature == null) {
                    throw new Exception("Signature no iniciada");
                }
                else {
                    signature.update(data);
                    signedData = signature.sign();
                    signature = null;
                    return signedData;
                }
        }
    }
	
}
