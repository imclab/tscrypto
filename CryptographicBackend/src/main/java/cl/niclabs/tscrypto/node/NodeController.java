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
import java.sql.SQLException;

import cl.niclabs.tscrypto.common.config.RabbitMQConfig;
import cl.niclabs.tscrypto.common.utils.TSConnection;
import cl.niclabs.tscrypto.common.utils.TSLogger;

import com.rabbitmq.client.Connection;

public class NodeController extends Thread {

	private boolean running;
	private Dispatcher dispatcher;
	private Collector collector;
    private KeyShareManager keyManager;
    private KeyCollector keyCollector;

	public NodeController() throws IOException, SQLException, ClassNotFoundException {
        RabbitMQConfig rconfig = NodeConfig.getInstance().getRabbitMQConfig();

        Connection connection = TSConnection.getConnection(rconfig);
        TSLogger.node.debug(String.format("Connected to RabbitMQ Server: %s", rconfig));

        dispatcher = new Dispatcher(connection);
        keyManager = new H2KeyShareManager();
        collector = new Collector(connection, dispatcher, keyManager);
        keyCollector = new KeyCollector(connection, dispatcher, keyManager);

        running = true;

        TSLogger.node.debug("Initialization: Done");
    }

    @Override
    public void run() {
        TSLogger.node.debug("Running...");

        try {
            synchronized (this) {
                while (running) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            TSLogger.node.fatal(
                "Interrupted " + this, e);

            stopServer();
        }
    }

    public synchronized void stopServer() {
        running = false;
        notifyAll();

        collector.handleShutdownSignal(null, null);
        keyCollector.handleShutdownSignal(null, null);
    }
    
    /**
     * Non-daemon mode.
     * Cannot be controlled nor stopped.
     * Should be used for testing only
     * @param args
     * @throws IOException
     * @throws Exception
     */
    public static void main(String[] args) throws IOException, InterruptedException, SQLException, ClassNotFoundException {
		NodeController controller = new NodeController();
		controller.start();
		controller.join();
	}
}
