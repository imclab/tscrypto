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

public class FindKeyMethod implements Method {

  public static class ReturnValue {
    public final String key;

      public ReturnValue(String key) {
          this.key = key;
      }
  }

  public static class Args {
    public String keyHandler;
  }

    private final String handler;
    private final KeyOperations keyOperations;

    public FindKeyMethod(Args args, KeyOperations keyOperations) {
        handler = args.keyHandler;
        this.keyOperations = keyOperations;
    }

    @Override
    public ResponseMessage execute() {
        try {
            String keyBase64 = keyOperations.findKey(handler);
            return ResponseMessage.OKMessage(new ReturnValue(keyBase64));
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }
    }

}
