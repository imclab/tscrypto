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

package cl.niclabs.tscrypto.node;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import cl.niclabs.tscrypto.common.config.RabbitMQConfig;
import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyShares;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.Util;

/**
 * Loads default configurations from {@code -Dcl.niclabs.threshsig.node.config}.
 */
public class NodeConfig {
    public static final String DEFAULT_CONFIG_PROPERTY = "cl.niclabs.tscrypto.node.config";

	Properties conf;
	
	private RabbitMQConfig rconfig;

    private static NodeConfig instance = null;

    public static NodeConfig getInstance() {
        if (instance == null) {
            synchronized (NodeConfig.class) {
                if (instance == null) {
                    try {
                        instance = new NodeConfig();
                    } catch (IOException e) {
                        throw new RuntimeException("Cannot open node configuration.", e);
                    }
                }
            }
        }
        return instance;
    }


	private NodeConfig() throws IOException {
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

	public boolean getUseJNI(){
        	return "true".equals(conf.getProperty("node.useJNI"));
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
