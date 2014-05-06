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

import cl.niclabs.cb.common.MethodCollector;
import cl.niclabs.cb.common.SessionManagerImpl;
import cl.niclabs.cb.common.methods.MethodFactoryImpl;
import cl.niclabs.tscrypto.common.datatypes.Collector;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.manager.PublishDispatcher;
import cl.niclabs.tscrypto.manager.RequestManager;
import cl.niclabs.tscrypto.manager.ResultsCollector;
import cl.niclabs.tscrypto.manager.SDConfig;
import cl.niclabs.tscrypto.manager.keyManagement.H2KeyManager;
import cl.niclabs.tscrypto.manager.keyManagement.KeyManager;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.sql.SQLException;

public class Backend extends Thread {

    private boolean running;
    private PublishDispatcher dispatcher;
    private MethodCollector methodCollector;
    private Collector resultsCollector;
    private KeyManager keyManager;
    private ZMQ.Context context;

    public Backend() throws IOException, SQLException, ClassNotFoundException {
        SDConfig config = SDConfig.getInstance();

        context = ZMQ.context(1);

        keyManager = new H2KeyManager();
        dispatcher = new PublishDispatcher(context);
        RequestManager requestManager = new RequestManager(
                dispatcher,
                "",
                config.getSignRequestEnvelope(),
                config.getKeyManagementEnvelope() // TODO: Put this in config file.
        );

        methodCollector = new MethodCollector(
                ZMQ.context(1),
                new MethodFactoryImpl(
                        new SessionManagerImpl(),
                        new SessionFactoryImpl(keyManager, requestManager),
                        new KeyOperationsImpl(
                                config.getK(),
                                config.getL(),
                                requestManager,
                                keyManager
                        )
                )
            );

        resultsCollector = new ResultsCollector(context, requestManager);

        methodCollector.start();
        resultsCollector.start();

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

        try {
            resultsCollector.stop();
            methodCollector.stop();
        } catch (InterruptedException e) {
            TSLogger.sd.fatal("Error while closing...");
        }
        try {
            dispatcher.close();
            keyManager.close();
            context.term();
        } catch (IOException e) {
            TSLogger.sd.fatal("Error while closing...");
        }

    }

    public static void main(String[] args) throws InterruptedException, IOException, SQLException, ClassNotFoundException {
        Backend backend = new Backend();
        backend.start();
        backend.join();
    }
}
