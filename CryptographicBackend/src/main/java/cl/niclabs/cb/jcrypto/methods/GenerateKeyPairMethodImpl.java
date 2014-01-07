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

package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GenerateKeyPairMethod;
import cl.niclabs.cb.jcrypto.KeyStorage;
import cl.niclabs.cb.jcrypto.MapKeyStorage;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

class GenerateKeyPairMethodImpl implements GenerateKeyPairMethod {
    private final String keyType;
    private final int keySize;
    private final String publicExponent;
    private final KeyStorage ks;

    public GenerateKeyPairMethodImpl(Args args) {
        keyType = args.keyType;
        keySize = args.keySize;
        publicExponent = args.publicExponent;
        ks = MapKeyStorage.getInstance();
    }

    @Override
    public ResponseMessage execute() {

      try {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyType);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        AlgorithmParameterSpec rsaParams =
                new RSAKeyGenParameterSpec(keySize, new BigInteger(publicExponent));

        kpg.initialize(rsaParams, random);

        KeyPair pair = kpg.generateKeyPair();
        String handler = ks.storeKeyPair(keyType, keySize, pair);

        return ResponseMessage.OKMessage(new ReturnValue(handler));
      } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
        return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
      }

    }
}
