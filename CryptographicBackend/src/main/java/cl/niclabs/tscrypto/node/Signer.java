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

import java.math.BigInteger;
import cl.niclabs.tscrypto.common.algorithms.PlayerSigner;
import cl.niclabs.tscrypto.common.algorithms.PlayerSignerImpl;
import cl.niclabs.tscrypto.common.datatypes.SignatureShare;
import cl.niclabs.tscrypto.common.utils.TSLogger;

public class Signer {
    private KeyShareManager keyManager;

    public Signer(KeyShareManager keyManager) {
        this.keyManager = keyManager;
    }
		
	public int getId() {
		return NodeConfig.getInstance().getNodeId();
	}

    public SignatureShare sign(BigInteger document, String alias) {
        SignatureShare signatureShare = null;
        TSLogger.node.info("document: " + document);

        PlayerSigner playerSigner = new PlayerSignerImpl(keyManager.getKeyShareInfo(alias), getId());

        try {
            signatureShare = playerSigner.sign(document);
        } catch (final java.security.NoSuchAlgorithmException e) {
            TSLogger.node.error("Provider could not locate SHA message digest .");
        }

        return signatureShare;
    }

    public KeyShareManager getKeyShareManager() {
        return keyManager;
    }

}
