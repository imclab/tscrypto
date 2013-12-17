package cl.inria.tscrypto.node;

import java.io.IOException;
import java.math.BigInteger;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import cl.inria.tscrypto.common.algorithms.PlayerSigner;
import cl.inria.tscrypto.common.algorithms.PlayerSignerImpl;
import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.SignatureShare;
import cl.inria.tscrypto.common.utils.TSLogger;

public class Signer {

	NodeConfig config;
    KeyShareManager keyManager;

	public Signer(NodeConfig config) {
        this(config, new KeyShareManager());
	}

    public Signer(NodeConfig config, KeyShareManager keyManager) {
        this.config = config;
        this.keyManager = keyManager;
    }
		
	public int getId() {
		return config.getNodeId();
	}

    /*
	public SignatureShare sign(BigInteger document, String alias) {

		SignatureShare signatureShare = null;
		TSLogger.node.info("document: " + document);
		
		PlayerSigner playerSigner = signers.get(alias);
		if (playerSigner == null) {
			TSLogger.node.error("Signer for alias " + alias + " could not be found.");
		}

		try {
			signatureShare = playerSigner.sign(document);
		} catch (final java.security.NoSuchAlgorithmException e) {
			TSLogger.node.error("Provider could not locate SHA message digest .");
		}

		return signatureShare;
	}
	*/

    public SignatureShare sign(BigInteger document, String alias) {
        SignatureShare signatureShare = null;
        TSLogger.node.info("document: " + document);

        PlayerSigner playerSigner = new PlayerSignerImpl(keyManager.getKeyShareInfo(alias), config.getNodeId());

        try {
            signatureShare = playerSigner.sign(document);
        } catch (final java.security.NoSuchAlgorithmException e) {
            TSLogger.node.error("Provider could not locate SHA message digest .");
        }

        return signatureShare;
    }

}
