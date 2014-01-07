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

package cl.niclabs.tscrypto.sigDealer.handlers;

import cl.niclabs.tscrypto.common.messages.SendKeyAnswer;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.sigDealer.KeyDispatchRequest;
import cl.niclabs.tscrypto.sigDealer.RequestManager;


public class KeyReceivedHandler implements SDHandler {
    private SendKeyAnswer message;

    @Override
    public void init(TSMessage message) {
        this.message = (SendKeyAnswer) message;
    }

    @Override
    public void handle(RequestManager manager) {
        KeyDispatchRequest request =
                manager.getKeyDispatchRequest(message.ticket);

        request.setReady(message.getNodeId());
        TSLogger.keyFactory.info("Node " + message.getNodeId() + " added the key to it's manager.");
    }
}
