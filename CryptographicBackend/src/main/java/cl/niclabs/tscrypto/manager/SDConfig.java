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

package cl.niclabs.tscrypto.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;

import cl.niclabs.tscrypto.common.encryption.KeyChain;
import cl.niclabs.tscrypto.common.encryption.KeyTool;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.Util;

import static java.security.KeyStore.PasswordProtection;
import static java.security.KeyStore.ProtectionParameter;

public class SDConfig {
    // Configuration now is a singleton.
    private static final SDConfig INSTANCE;
    static  { // static initialization of singleton class.
        try {
            INSTANCE = new SDConfig();
            KeyChain.init(INSTANCE.getKeyStore(), INSTANCE.getProtectionParameter());
            KeyTool.init(INSTANCE.getKeyStore());
        } catch (IOException e) {
            throw new RuntimeException("Cannot open SD configuration", e);
        }
    }

    public static SDConfig getInstance() {
        return INSTANCE;
    }


    public static final String DEFAULT_CONFIG_PROPERTY = "cl.niclabs.tscrypto.sd.config";

	Properties conf;

	private SDConfig() throws IOException {
        TSLogger.sd.info("Running with config: " + System.getProperty(DEFAULT_CONFIG_PROPERTY));

        this.conf = Util.loadTrimedProperties(System.getProperty(DEFAULT_CONFIG_PROPERTY));
	}


	public int getThreadsResults() {
        return Integer.parseInt(conf.getProperty("sd.threads"));
	}

    public int getK() {
        return Integer.parseInt(conf.getProperty("sd.k"));
    }

    public int getL() {
        return Integer.parseInt(conf.getProperty("sd.l"));
    }

    public boolean getUseJni() {
        return Boolean.parseBoolean(conf.getProperty("sd.useJNI"));
    }

    public String getMethodCollectorPort() {
        return conf.getProperty("sd.connection.method_collector.port");
    }

    public String getDispatcherPort() {
        return  conf.getProperty("sd.connection.dispatcher.port");
    }

    public String getResultsPort() {
        return conf.getProperty("sd.connection.results.port");
    }

    public String getSignRequestEnvelope() { return "sign-request"; }

    public String getKeyManagementEnvelope() { return "key-mgmt"; }

    public String getIncomingRoutingPort() {
        return conf.getProperty("sd.connection.router.reqs.port");
    }

    public String getOutgoingRoutingPort() {
        return conf.getProperty("sd.connection.router.reps.port");
    }

    public String getNodeAlias(int node) {
        return "node" + node;
    }

    public String getKeyAlias() {
        return conf.getProperty("sd.key.alias");
    }

    public char[] getKeyPassword() {
        return conf.getProperty("sd.key.password").toCharArray();
    }

    public ProtectionParameter getProtectionParameter() {
        return new PasswordProtection(getKeyPassword());
    }

    KeyStore getKeyStore() {
        String path = conf.getProperty("sd.keystore.path");
        char[] password = getKeyPassword();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (FileInputStream fis = new FileInputStream(path)) {
                keyStore.load(fis, password);
            }

        } catch (KeyStoreException
                | IOException
                | CertificateException
                | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return keyStore;
    }
}
