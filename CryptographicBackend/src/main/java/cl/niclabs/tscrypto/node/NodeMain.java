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

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

import cl.niclabs.tscrypto.common.utils.TSLogger;

/**
 * Main entry point to launch Node module via
 * apache's jsvc daemon interface.
 * 
 * @author acansado
 */
public class NodeMain implements Daemon {
    private NodeController controller;

    @Override
    public void init(DaemonContext dc) throws DaemonInitException {
    }

    @Override
    public void start() throws Exception {
        TSLogger.node.info("Starting Node");
        controller = new NodeController();
        controller.start();
        controller.join();
    }

    @Override
    public void stop() throws Exception {
        controller.stopServer();
        TSLogger.node.info("Stoping Server");
    }

    @Override
    public void destroy() {
        TSLogger.node.info("Destroying Server");
    }

    public static void main(String[] args) {
        TSLogger.node.info("Running Controller");

        try {
            NodeMain main = new NodeMain();
            main.init(null);
            main.start();
        } catch (Exception e) {
            TSLogger.node.fatal("Unknown Error when running the Server",
                e);
        }
    }
}
