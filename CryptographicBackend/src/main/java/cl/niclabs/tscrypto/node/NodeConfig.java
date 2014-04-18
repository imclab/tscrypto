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
import java.security.KeyStore;
import java.util.Properties;

import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.Util;

/**
 * Loads default configurations from {@code -Dcl.niclabs.threshsig.node.config}.
 */
public class NodeConfig {
    private static final String DEFAULT_CONFIG_PROPERTY = "cl.niclabs.tscrypto.node.config";

	private Properties conf;

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
	}

	public int getNodeId() {
        return Integer.parseInt(conf.getProperty("node.id"));
	}

	public int getNumThreads() {
        return Integer.parseInt(conf.getProperty("node.threads"));
	}

	public boolean getUseJNI(){
        return Boolean.parseBoolean(conf.getProperty("node.useJNI"));
    }

    // TODO: Extract all of this.
    public String getManagerAddress() {
        return "localhost";
    }

    public String getDispatcherPort() {
        return "10001";
    }

    public String getResultsPort() {
        return "10002";
    }

    public String getSignRequestEnvelope() { return "sign-request"; }

    public String getKeyManagementEnvelope() { return "key-mgmt" + getNodeId(); }

    public String getKeyStoreFilename() {
        return "conf/node" + getNodeId() + ".jks";
    }

    public String getKeyStorePassword() { return "niclabs.13"; } // Extract all...
}
