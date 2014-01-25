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

package cl.niclabs.tscrypto.sigDealer;

import java.io.IOException;
import java.util.Properties;

import cl.niclabs.tscrypto.common.config.RabbitMQConfig;
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
	
	private RabbitMQConfig rconfig;

	private SDConfig() throws IOException {
        TSLogger.sd.info("Running with config: " + System.getProperty(DEFAULT_CONFIG_PROPERTY));

        this.conf = Util.loadTrimedProperties(System.getProperty(DEFAULT_CONFIG_PROPERTY));

        this.rconfig = new RabbitMQConfig();
	}

	public RabbitMQConfig getRabbitMQConfig() {
		return rconfig;
	}

	public int getThreadsResults() {
        return Integer.parseInt(conf.getProperty("sd.threads"));
	}

    public String getRpcQueue() {
        return conf.getProperty("rabbitmq.queues.rpc");
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
}
