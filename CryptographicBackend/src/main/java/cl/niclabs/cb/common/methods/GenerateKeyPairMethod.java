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

package cl.niclabs.cb.common.methods;

import cl.niclabs.cb.common.KeyOperations;
import cl.niclabs.cb.common.Method;
import cl.niclabs.cb.common.ResponseMessage;


public class GenerateKeyPairMethod implements Method {

  public static class ReturnValue {
    public final String keyHandler;
    public final String modulus;
      public final String publicExponent;

      public ReturnValue(String keyHandler, String modulus, String publicExponent) {
          this.keyHandler = keyHandler;
          this.modulus = modulus;
          this.publicExponent = publicExponent;
      }
  }

  public static class Args {
    public final String keyType;
    public final int keySize;
    public final String publicExponent;

      public Args(String keyType, int keySize, String publicExponent) {
          this.keyType = keyType;
          this.keySize = keySize;
          this.publicExponent = publicExponent;
      }
  }

    private final String keyType;
    private final int keySize;
    private final String publicExponent;
    private final KeyOperations keyOperations;

    public GenerateKeyPairMethod(Args args, KeyOperations keyOperations) {
        keyType = args.keyType;
        keySize = args.keySize;
        publicExponent = args.publicExponent;
        this.keyOperations = keyOperations;
    }

    @Override
    public ResponseMessage execute() {

        try {
            KeyOperations.GenerationResult result = keyOperations.generateKeys(keyType, keySize, publicExponent);
            return ResponseMessage.OKMessage(new ReturnValue(result.keyHandler, result.modulus, result.publicExponent));
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }

    }

}
