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

package cl.niclabs.cb.tscrypto;

import cl.niclabs.tscrypto.common.utils.TSConnection;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.sigDealer.KeyDispatcher;
import cl.niclabs.tscrypto.sigDealer.Dispatcher;
import cl.niclabs.tscrypto.sigDealer.RequestManager;
import cl.niclabs.tscrypto.sigDealer.ResultsCollector;
import cl.niclabs.tscrypto.sigDealer.SDConfig;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Backend extends Thread {

    private boolean running;
    private Dispatcher dispatcher;
    private KeyDispatcher keyDispatcher;
    private MethodCollector methodCollector;
    private ResultsCollector resultsCollector;

    public Backend() throws IOException {
        SDConfig config = SDConfig.getInstance();
        Connection connection = TSConnection.getConnection(config.getRabbitMQConfig());

        TSLogger.sd.debug(String.format("Connected to RabbitMQ Server: %s", config.getRabbitMQConfig()));

        dispatcher = new Dispatcher(connection, config.getRabbitMQConfig());
        keyDispatcher = new KeyDispatcher(connection, config.getRabbitMQConfig());

        RequestManager requestManager = new RequestManager(
                dispatcher, keyDispatcher,
                config.getRabbitMQConfig().getClientQueue(),
                config.getRabbitMQConfig().getSignRequestAlias()
        );

        methodCollector = new MethodCollector(connection, requestManager);
        resultsCollector = new ResultsCollector(connection, requestManager);
        running = true;

        TSLogger.sd.info("Initialization: Done");
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (running) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            TSLogger.sd.fatal("Interrupted " + this, e);
            stopServer();
        }
    }

    private synchronized void stopServer()  {
        running = false;
        this.notifyAll();
        methodCollector.handleShutdownSignal(null, null);
        resultsCollector.handleShutdownSignal(null, null);
        try {
            dispatcher.close();
            keyDispatcher.close();
        } catch (IOException e) {
            TSLogger.sd.fatal("Error while closing...");
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Backend backend = new Backend();
        backend.start();
        backend.join();
    }
}
