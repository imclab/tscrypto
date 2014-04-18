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

import cl.niclabs.tscrypto.common.datatypes.Collector;
import cl.niclabs.tscrypto.common.messages.DeleteKeyAnswer;
import cl.niclabs.tscrypto.common.messages.SendKeyAnswer;
import cl.niclabs.tscrypto.common.messages.SignShareAnswer;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.utils.HandlerFactory;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.manager.handlers.KeyDeletedHandler;
import cl.niclabs.tscrypto.manager.handlers.KeyReceivedHandler;
import cl.niclabs.tscrypto.manager.handlers.SignedShareHandler;
import com.google.gson.JsonParseException;
import org.zeromq.ZMQ;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResultsCollector implements Collector, Closeable {

    private ZMQ.Socket socket;
	private ExecutorService executor;
	private RequestManager manager;
    private HandlerFactory<DealerHandler> handlerFactory;
    private boolean running;
    private Thread thread;


	public ResultsCollector(ZMQ.Context context, RequestManager requestManager) {
        SDConfig config = SDConfig.getInstance();
        socket = context.socket(ZMQ.PULL);
        socket.bind("tcp://*:" + config.getResultsPort());

        executor = Executors.newScheduledThreadPool(config.getThreadsResults());
        manager = requestManager;

        handlerFactory = new HandlerFactory<>();
        handlerFactory.addHandler(SignShareAnswer.class, SignedShareHandler.class);
        handlerFactory.addHandler(SendKeyAnswer.class, KeyReceivedHandler.class);
        handlerFactory.addHandler(DeleteKeyAnswer.class, KeyDeletedHandler.class);

        running = false;

	}

    public void start() {
        if(!running) {
            running = true;
            thread = new CollectorThread();
            thread.start();
        }
    }

    public void stop() throws InterruptedException {
        if(running) {
            running = false;
            thread.join();
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private class CollectorThread extends Thread {
        @Override
        public void run() {
            while (running) {
                String message = socket.recvStr();
                executor.submit(new Handler(message));
            }
        }
    }

    private class Handler implements Runnable {
        final String body;

        private Handler(String body) {
            this.body = body;
        }

        @Override
        public void run() {
            TSMessage message = TSMessage.fromJson(body);
            try {
                handlerFactory.createMessageHandler(message).handle(manager);
            } catch (JsonParseException e) {
                TSLogger.node.error("Parsing Error for msg: "
                        + body, e);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                TSLogger.node.error("Unknown Exception for msg: "
                        + body, e);
            }
        }
    }


}
