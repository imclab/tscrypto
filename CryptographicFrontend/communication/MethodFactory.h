/*
 * <one line to give the library's name and an idea of what it does.>
 * Copyright (C) 2014  Francisco Cifuentes <email>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

#ifndef COMMUNICATION_METHODFACTORY_H
#define COMMUNICATION_METHODFACTORY_H
#include <Method.h>

namespace communication {

struct MethodFactory
{
    virtual Method closeSession(string sessionHandler);
    virtual Method deleteKeyPair(string keyHandler);
    virtual Method digestInit(string sessionHandler, string mechanism);
    virtual Method digest(string sessionHandler, string data);
    virtual Method findKey(string keyHandler);
    virtual Method generateKeyPair(string keyType, long keySize, string publicExponent);
    virtual Method generateRandom(string sessionHandler, long length);
    virtual Method getAttribute(string attribute, string handler);
    virtual Method openSession();
    virtual Method seedRandom(string sessionHandler, string seed);
    virtual Method signInit(string sessionHandler, string mechanism, string keyHandler);
    virtual Method sign(string sessionHandler, string data);   
    
    virtual ~MethodFactory() = default;
};

}

#endif // COMMUNICATION_METHODFACTORY_H
