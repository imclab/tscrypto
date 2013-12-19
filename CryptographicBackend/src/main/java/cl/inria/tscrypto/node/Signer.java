package cl.inria.tscrypto.node;

import java.math.BigInteger;
import cl.inria.tscrypto.common.algorithms.PlayerSigner;
import cl.inria.tscrypto.common.algorithms.PlayerSignerImpl;
import cl.inria.tscrypto.common.datatypes.SignatureShare;
import cl.inria.tscrypto.common.utils.TSLogger;

public class Signer {

	private NodeConfig config;
    private  KeyShareManager keyManager;

    public Signer(NodeConfig config, KeyShareManager keyManager) {
        this.config = config;
        this.keyManager = keyManager;
    }
		
	public int getId() {
		return config.getNodeId();
	}

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
