package cl.inria.tscrypto.sigDealer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.common.utils.Util;

public class SDConfig {
    public static final String DEFAULT_CONFIG_PROPERTY = "cl.inria.tscrypto.sd.config";

	Properties conf;
	
	private RabbitMQConfig rconfig;

	public SDConfig() throws IOException {
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
}
