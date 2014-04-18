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

import cl.niclabs.tscrypto.common.encryption.KeyChain;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.node.keyManagement.H2KeyShareManager;
import cl.niclabs.tscrypto.node.keyManagement.KeyShareManager;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public class NodeControllerZero extends Thread {

	private boolean running;
    private RequestCollector collector;

    private ZMQ.Context context;

	public NodeControllerZero() throws IOException, SQLException, ClassNotFoundException {
        /** TODO: Complete encrypted communication system
        try {
            NodeConfig config = NodeConfig.getInstance();
            KeyChain keyChain = KeyChain.getInstance();
            keyChain.loadKeyStoreFile(config.getKeyStoreFilename(), config.getKeyStorePassword().toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            TSLogger.node.fatal("Couldn't open the keystore.", e);
            System.exit(-1);
        }
        */

        context = ZMQ.context(1);
        collector = new RequestCollector(
                context,
                new DispatcherZero(context),
                new H2KeyShareManager()
        );

        running = true;
        TSLogger.node.debug("Initialization: Done");
    }

    @Override
    public void run() {
        collector.start();
        TSLogger.node.info("Collector started...");

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

    synchronized void stopServer() {
        running = false;
        notifyAll();

        try {
            collector.stop();
            collector.close();
        } catch (IOException | InterruptedException ignored) {}

        context.term();
    }

    /**
     * Non-daemon mode.
     * Cannot be controlled nor stopped.
     * Should be used for testing only
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException, SQLException, ClassNotFoundException {
		NodeControllerZero controller = new NodeControllerZero();
		controller.start();
		controller.join();
	}
}
