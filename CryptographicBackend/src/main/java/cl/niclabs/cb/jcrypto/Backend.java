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

package cl.niclabs.cb.jcrypto;

import cl.niclabs.cb.common.MethodCollector;
import cl.niclabs.cb.common.SessionManagerImpl;
import cl.niclabs.cb.common.methods.MethodFactory;
import cl.niclabs.cb.common.methods.MethodFactoryImpl;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Backend {

    private static void run(String hostName, String queueName)
            throws ShutdownSignalException, ConsumerCancelledException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostName);
        Connection connection;
        try {
            connection = factory.newConnection();
            MethodFactory methodFactory = new MethodFactoryImpl(
                    new SessionManagerImpl(),
                    new SessionFactoryImpl(),
                    new KeyOperationsImpl(MapKeyStorage.getInstance())
            );
            new MethodCollector(connection, queueName, methodFactory);
            try {
                synchronized (Backend.class) {
                    while (true) {
                        Backend.class.wait();
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Error al esperar mensajes");
            }

        } catch (IOException e) {
            System.err.println("No se puede conectar al servidor...");
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }

	public static void main(String[] args) 
			throws ShutdownSignalException, ConsumerCancelledException, InterruptedException {
        String queueName = "";
        String hostName = "";

        // Manejo de argumentos...
        switch (args.length) {
            case 0:
                hostName = "localhost";
                queueName = "rpc_queue";
                break;

            case 1:
                hostName = args[0];
                queueName = "rpc_queue";
                break;

            case 2:
                hostName = args[0];
                queueName = args[1];
                break;

            default:
                System.err.println("Muchos argumentos.");
                System.exit(1);
        }

        run(hostName, queueName);
	}

}
