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

package cl.niclabs.cb.tscrypto;

import cl.niclabs.tscrypto.common.algorithms.dealer.SignatureRequest;
import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;
import cl.niclabs.tscrypto.common.datatypes.Ticket;
import cl.niclabs.tscrypto.manager.Request;
import cl.niclabs.tscrypto.manager.keyManagement.KeyManager;
import cl.niclabs.tscrypto.manager.RequestManager;
import cl.niclabs.cb.common.Signer;
import cl.niclabs.tscrypto.manager.requests.SignRequest;

import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Map;

public class SignerImpl implements Signer {

    private final KeyManager keyManager;
    private String algorithm;
    private String privateKeyHandler;
    private final RequestManager requestManager;
    private boolean initialized;

    private final static Map<String, String> algorithms = new Hashtable<String, String>() {{
        put("Sha1WithRSA", "Sha1");
        put("NONEWithRSA", "NONE");
    }};

    public SignerImpl(KeyManager keyManager, RequestManager requestManager) {
        this.keyManager = keyManager;
        this.requestManager = requestManager;
        initialized = false;
    }

	public void init (String algorithm, String keyHandler)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException
    {
        if (keyManager.containsKey(keyHandler)) {
            throw new InvalidKeyException("Not exists any key with handler: " + keyHandler);
        }

        this.algorithm = algorithm;

        if (!algorithms.containsKey(algorithm)) {
            throw new NoSuchAlgorithmException("No such algorithm: " + algorithm);
        }

        this.privateKeyHandler = keyHandler;

        initialized = true;
	}
	
	public byte[] sign (byte[] data) throws Exception {
        if(!initialized) {
            throw new Exception("SignerImpl no iniciado.");
        }

        String hashAlgorithm = algorithms.get(algorithm);

        KeyMetaInfo keyMetaInfo = keyManager.getKeyMetaInfo(privateKeyHandler);
        TSPublicKey publicKey = keyManager.getPublicKey(privateKeyHandler);

        Ticket ticket = requestManager.sign(keyMetaInfo, publicKey, hashAlgorithm, data, privateKeyHandler);
        SignRequest request = requestManager.getRequest(ticket);
        request.waitUntilReady();
        BigInteger signature = request.getDealer().getSignature();
        requestManager.removeRequest(ticket);

        algorithm = "";
        privateKeyHandler = "";

        return signature.toByteArray();

    }
	
}
