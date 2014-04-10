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

import cl.niclabs.cb.common.Method;

public interface MethodFactory {
    Method makeOpenSessionMethod();
    Method makeCloseSessionMethod(CloseSessionMethod.Args args);
    Method makeDeleteKeyMethod(DeleteKeyPairMethod.Args args);
    Method makeFindKeyMethod(FindKeyMethod.Args args);
    Method makeGenerateKeyPairMethod(GenerateKeyPairMethod.Args args);
    Method makeSignInitMethod(SignInitMethod.Args args);
    Method makeSignMethod(SignMethod.Args args);
    Method makeSeedRandomMethod(SeedRandomMethod.Args args);
    Method makeGenerateRandomMethod(GenerateRandomMethod.Args args);
    Method makeDigestInitMethod(DigestInitMethod.Args args);
    Method makeDigestMethod(DigestMethod.Args args);
}
