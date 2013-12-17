package cl.inria.tscrypto.node;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.KeyMetaInfo;
import cl.inria.tscrypto.common.datatypes.KeyShares;
import cl.inria.tscrypto.common.datatypes.TSPublicKey;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.common.utils.Util;

/**
 * Loads default configurations from {@code -Dcl.niclabs.threshsig.node.config}.
 * 
 * @throws IOException if configuration files can't be read
 */
public class NodeConfig {
    public static final String DEFAULT_CONFIG_PROPERTY = "cl.inria.tscrypto.node.config";

	Properties conf;
	
	private RabbitMQConfig rconfig;

	public NodeConfig() throws IOException {
        TSLogger.node.info("Running with config: " + System.getProperty(DEFAULT_CONFIG_PROPERTY));

        this.conf = Util.loadTrimedProperties(System.getProperty(DEFAULT_CONFIG_PROPERTY));
        this.rconfig = new RabbitMQConfig();
	}

	public RabbitMQConfig getRabbitMQConfig() {
		return rconfig;
	}

	public int getNodeId() {
        return Integer.parseInt(conf.getProperty("node.id"));
	}

	public final String getPassword() {
        return conf.getProperty("node.password");
	}

	public String getKeyPath() {
        return conf.getProperty("key.path");
	}

	public int getNumThreads() {
        return Integer.parseInt(conf.getProperty("node.threads"));
	}

	// TODO should not be here
	public KeyInfo getKey(String alias) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		KeyMetaInfo keyMetaInfo = Util.loadKeyMetaInfoFromFile(getKeyPath() + "/" + alias + ".metainfo");
		TSPublicKey publicKey = Util.loadPublicKeyFromFile(getKeyPath() + "/" + alias + ".publickey");
		KeyShares keyShares = Util.loadKeySharesFromFile(getKeyPath() + "/" + alias + ".keyshare-" + getNodeId(), keyMetaInfo, getNodeId());
		
		return new KeyInfo(keyMetaInfo, publicKey, keyShares);
	}

	public String getDefaultKey() {
		return conf.getProperty("key.defaultKey");
	}
	
	public String getKeyStore() {
		return conf.getProperty("keystore.filename");
	}

}
