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

/*
 *   Copyright (C) 2011 NIC Chile Research Labs, Universidad de Chile.
 *
 *   Adkintun is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Adkintun is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 *   You should have received a copy of the GNU General Public License
 *   along with Adkintun.  If not, see <http://www.gnu.org/licenses/>.
 */
package cl.niclabs.tscrypto.common.config;

import cl.niclabs.tscrypto.common.utils.Util;

import java.io.IOException;
import java.util.Properties;

/**
 * Provides an interface to access RabbitMQ configuration
 * 
 * @author mleyton
 * @author acansado
 */
public class RabbitMQConfig {

    public static final String DEFAULT_CONFIG_PROPERTY = "cl.niclabs.tscrypto.rabbitmq.config";

    String username, password, host;
    Integer port, maxChannels;

    //Queue configs
    String requestsQueue;
	String clientQueue;
    String keyManagementQueue;

    Integer queueDeclareTimeout;
    Boolean queueDeclareDurable, queueDeclareExclusive, queueDeclareAutodelete;

    //Exchange configs
	String requestsExchange;
    String signRequestAlias;
//	String signatureSharesExchange;
    Boolean exchangeDeclareDurable, exchangeDeclareAutodelete, exchangeDeclareInternal;
    String keysQueue;


    public String getKeyManagementQueue() {
        return keyManagementQueue;
    }

    public String getSignRequestAlias() {
        return signRequestAlias;
    }

    /**
     * Creates a new RabbitMQ configuration based on the properties file defined
     * in the system property -Dcl.niclabs.adkintun.rabbitmq.config
     * 
     * @throws IOException if the file cannot be accessed
     */
    public RabbitMQConfig() throws IOException {
        
        Properties properties = Util.loadTrimedProperties(System.getProperty(DEFAULT_CONFIG_PROPERTY));

        this.username = properties.getProperty("rabbitmq.username");
        this.password = properties.getProperty("rabbitmq.password");
        this.host = properties.getProperty("rabbitmq.host");
        this.port = Integer.parseInt(properties.getProperty("rabbitmq.port"));

        this.queueDeclareDurable = Boolean.valueOf(properties.getProperty("rabbitmq.queue.declare.durable"));
        this.queueDeclareExclusive = Boolean.valueOf(properties.getProperty("rabbitmq.queue.declare.exclusive"));
        this.queueDeclareAutodelete = Boolean.valueOf(properties.getProperty("rabbitmq.queue.declare.autodelete"));
        this.queueDeclareTimeout = Integer.parseInt(properties.getProperty("rabbitmq.queue.declare.timeout"));

        this.exchangeDeclareDurable = Boolean.valueOf(properties.getProperty("rabbitmq.exchange.declare.durable"));
        this.exchangeDeclareAutodelete = Boolean.valueOf(properties.getProperty("rabbitmq.exchange.declare.autodelete"));
        this.exchangeDeclareInternal = Boolean.valueOf(properties.getProperty("rabbitmq.exchange.declare.internal"));

        this.requestsExchange = properties.getProperty("rabbitmq.exchange.requests");
        this.signRequestAlias = properties.getProperty("rabbitmq.exchange.requests.signRequestAlias");

        this.keysQueue = properties.getProperty("rabbitmq.queue.keys");

//        this.signatureSharesExchange = properties.getProperty("rabbitmq.exchange.signatureShares");
        
        this.requestsQueue = properties.getProperty("rabbitmq.queue.requests");
        this.clientQueue = properties.getProperty("rabbitmq.queue.client");
        this.keyManagementQueue = properties.getProperty("rabbitmq.queue.keyManagement");
    }

    /**
     * @return the hostname where RabbitMQ server is running
     */
    public String getHost() {
        return host;
    }

    /**
     * 
     * @return the password to use when connecting to RabbitMQ server
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @return the port where RabbitMQ server is listening for connections
     */
    public Integer getPort() {
        return port;
    }

    /**
     * 
     * @return the username used to connect into the RabbitMQ server
     */
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return String.format("RabbitMQConfig: %s:%s@%s:%d",
                getUsername(), getPassword(), getHost(), getPort());
    }

    /**
     * @return the message timeout in milliseconds. Messages which spend more than the specified
     * time in a queue will not be delivered.
     */
    public int getQueueDeclareTimeout() {
        return queueDeclareTimeout;
    }

    /**
     * @return true if queue should persist after RabbitMQ reboot, false otherwise
     */
    public Boolean getQueueDeclareDurable() {
        return queueDeclareDurable;
    }

    /**
     * @return false if queue can be shared between clients
     */
    public Boolean getQueueDeclareExclusive() {
        return queueDeclareExclusive;
    }

    /**
     * 
     * @return false if the queue should not be autodeleted when no clients are listening on the queue
     */
    public Boolean getQueueDeclareAutodelete() {
        return queueDeclareAutodelete;
    }

    /**
     * 
     * @return true if exchange should survive server reboot
     */
    public Boolean getExchangeDeclareDurable() {
        return exchangeDeclareDurable;
    }
    
    /**
     * 
     * @return false means clients can write directly to this exchange, true means that only
     * other exchanges may write to this exchange (ie clients cannot write directly)
     */
    public Boolean getExchangeDeclareInternal() {
        return exchangeDeclareInternal;
    }

    /**
     * @return false if exchange should not be deleted if no client is writing to the exchange
     */
    public Boolean getExchangeDeclareAutodelete() {
        return exchangeDeclareAutodelete;
    }

	public String getRequestsQueue(int id) {
		return requestsQueue + id;
	}

	public String getRequestsExchange(String keyAlias) {
		return requestsExchange + "." + keyAlias;
	}

//	public String getSignatureSharesExchange() {
//		return signatureSharesExchange;
//	}

	public String getClientQueue() {
		return clientQueue;
	}


    public String getKeysQueue() {
        return keysQueue;
    }
}
