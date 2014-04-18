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

#ifndef ZEROCONNECTIONMANAGER_H
#define ZEROCONNECTIONMANAGER_H

#include <ConnectionManager.h>
#include "ZeroConnection.h"
#include <string>

namespace hsm {
class ZeroConnectionManager : public ConnectionManager
{
private:
    std::string hostname_;
    std::string port_;
    communication::ZeroConnection connection_;
public:
    ZeroConnectionManager();
    virtual Connection& getConnection();
    virtual ~ZeroConnectionManager();
};
}

#endif // ZEROCONNECTIONMANAGER_H
