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

import java.io.IOException;
import java.util.Properties;

import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.Util;

public class SDConfig {

    // Configuration now is a singleton.
    private static SDConfig instance = null;

    public static SDConfig getInstance() {
        if (instance == null) {
            synchronized (SDConfig.class) {
                if (instance == null) {
                    try {
                        instance = new SDConfig();
                    } catch (IOException e) {
                        throw new RuntimeException("Cannot open SD configuration", e);
                    }
                }
            }
        }

        return instance;
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
        return "10000";
    }

    public String getDispatcherPort() {
        return "10001";
    }

    public String getResultsPort() {
        return "10002";
    }

    public String getSignRequestEnvelope() { return "sign-request"; }

    public String getKeyManagementEnvelope() { return "key-mgmt"; }

    public String[] getNodeKeys() {
        return "conf/node0.cer,conf/node1.cer,conf/node2.cer".split(",");
    }
}
