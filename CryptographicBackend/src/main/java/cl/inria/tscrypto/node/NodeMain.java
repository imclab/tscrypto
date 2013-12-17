package cl.inria.tscrypto.node;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

import cl.inria.tscrypto.common.utils.TSLogger;

/**
 * Main entry point to launch Node module via
 * apache's jsvc daemon interface.
 * 
 * @author acansado
 */
public class NodeMain implements Daemon {
    private NodeConfig config;
    private NodeController controller;

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        config = new NodeConfig();
    }

    @Override
    public void start() throws Exception {
        TSLogger.node.info("Starting Node");
        controller = new NodeController(config);
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
