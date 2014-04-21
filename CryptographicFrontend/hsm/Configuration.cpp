/*
Copyright 2013 NIC Chile Research Labs.
This file is part of PKCS11-TsCrypto.

PKCS11-TsCrypto is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

PKCS11-TsCrypto is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with PKCS11-TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
*/

#include <fstream>
#include <cerrno>

#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>

#include "Configuration.h"
#include "TcbError.h"
#include "pkcs11.h"

using namespace hsm;

Configuration::Configuration ( std::string configurationPath )
{
    this->Configuration::load(configurationPath);
}

void Configuration::load(std::string configurationPath)
{
    using std::string;
    using boost::property_tree::ptree;
    using namespace boost::property_tree::json_parser;
    ptree root;    
    
    try {
	read_json(configurationPath, root);        
    } catch (json_parser_error & e) {
	throw TcbError("Configuration::load", e.what(), CKR_GENERAL_ERROR);
    }

    ptree database = root.get_child("database");          
    databaseConf_.path = database.get<string>("path");

    ptree slots = root.get_child("slots");
    for (ptree::value_type & v: slots) {
	ptree & token = v.second;
	slotConf_.push_back({ token.get<string>("label") });
    }
    
    ptree connection = root.get_child("connection");
    connectionConf_.host = connection.get<string>("host");
    connectionConf_.port = connection.get<string>("port");
}

std::vector<Configuration::SlotConf> const & Configuration::getSlotConf() const
{
    return slotConf_;
}

Configuration::DatabaseConf const & Configuration::getDatabaseConf() const
{
    return databaseConf_;
}

const Configuration::ConnectionConf& Configuration::getConnectionConf() const
{
    return connectionConf_;
}
