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

import cl.niclabs.cb.backend.Digester;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigesterImpl implements Digester {
    private MessageDigest messageDigest = null;

    @Override
    public void digestInit(String mechanism) throws NoSuchAlgorithmException {
        switch (mechanism) {
            case "MD5":
                messageDigest = MessageDigest.getInstance("MD5");
                break;
//            case "RIPEMD160":
//                messageDigest = MessageDigest.getInstance("RIPEMD160");
//                break;
            case "SHA-1":
                messageDigest = MessageDigest.getInstance("SHA-1");
                break;
            case "SHA-256":
                messageDigest = MessageDigest.getInstance("SHA-256");
                break;
            case "SHA-384":
                messageDigest = MessageDigest.getInstance("SHA-384");
                break;
            case "SHA-512":
                messageDigest = MessageDigest.getInstance("SHA-512");
                break;
            default:
                throw new NoSuchAlgorithmException(mechanism + " not available.");
        }
    }

    @Override
    public byte[] digest(byte[] data) throws Exception {
        if (data != null) {
            return messageDigest.digest(data);
        }

        throw new Exception("!"); // TODO!
    }

}
