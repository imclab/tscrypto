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

package cl.niclabs.tscrypto.common.messages;

import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.datatypes.Ticket;

public class SendKeyQuery extends MessageAsync {
    private String label;
    private KeyShareInfo keyShareInfo;

    public SendKeyQuery() {

    }

    public SendKeyQuery(Ticket ticket, String label, KeyShareInfo keyShareInfo, String replyTo) {
        super("sendKey-query", "1.0", ticket, replyTo);
        this.label = label;
        this.keyShareInfo = keyShareInfo;
    }

    public String getLabel() {
        return label;
    }

    public KeyShareInfo getKeyInfo() {
        return keyShareInfo;
    }
}
