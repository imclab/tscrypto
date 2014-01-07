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

package cl.niclabs.cb.backend.methods;

import cl.niclabs.cb.backend.Method;

public interface SignMethod extends Method {

  public static class ReturnValue {
      public final String sessionHandler;
      public final String signedData;

      public ReturnValue(String sessionHandler, String signedData) {
          this.sessionHandler = sessionHandler;
          this.signedData = signedData;
      }
  }

  public static class Args {
      public final String sessionHandler;
      public final String data;

      public Args(String sessionHandler, String data) {
          this.sessionHandler = sessionHandler;
          this.data = data;
      }
  }

}
