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

import org.zeromq.ZMQ;


public class Backend {

	public static void main(String[] args) throws  InterruptedException {
        MethodFactory methodFactory = new MethodFactoryImpl(
                new SessionManagerImpl(),
                new SessionFactoryImpl(),
                new KeyOperationsImpl(MapKeyStorage.getInstance())
        );

        MethodCollector collector = new MethodCollector(ZMQ.context(1), methodFactory);
        collector.start();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (Backend.class) {
                    Backend.class.wait();
                }
            } catch (InterruptedException e) {
                System.err.println("Error al esperar mensajes");
            }
        }
        collector.stop();
	}

}
