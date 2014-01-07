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

import cl.niclabs.cb.backend.RandomGenerator;

import java.security.SecureRandom;

public class RandomGeneratorImpl implements RandomGenerator {
    private SecureRandom secureRandom;

    public RandomGeneratorImpl() {
        this.secureRandom = new SecureRandom();
    }

    @Override
    public void setSeed(byte[] seed) {
        secureRandom.setSeed(seed);
    }

    @Override
    public byte[] generateRandom(int length) {
        return secureRandom.generateSeed(length);
    }
}
