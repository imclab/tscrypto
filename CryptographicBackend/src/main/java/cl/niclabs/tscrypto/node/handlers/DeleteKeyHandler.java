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

package cl.niclabs.tscrypto.node.handlers;

import cl.niclabs.tscrypto.common.messages.DeleteKeyAnswer;
import cl.niclabs.tscrypto.common.messages.DeleteKeyQuery;
import cl.niclabs.tscrypto.common.messages.MessageAsync;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.node.NodeConfig;
import cl.niclabs.tscrypto.node.NodeHandler;
import cl.niclabs.tscrypto.node.keyManagement.KeyShareManager;

public class DeleteKeyHandler implements NodeHandler {
    private DeleteKeyQuery message;

    @Override
    public void init(TSMessage message) {
        this.message = (DeleteKeyQuery) message;
    }

    @Override
    public MessageAsync handle(KeyShareManager manager) {
        NodeConfig config = NodeConfig.getInstance();
        manager.removeKey(message.getLabel());
        TSLogger.node.info("Key " + message.getLabel() + " deleted.");
        return new DeleteKeyAnswer(message.ticket, config.getNodeId(), message.getReplyTo());
    }

}
