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

#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <json/json.h>
#include "Message.h"

using std::string;
using std::vector;
using std::stringstream;

namespace communication {

Message::Message ( const string & name )
{
    name_ = name;
}

void Message::addArgument ( argument::Name name, argument::Value value )
{
    argMap_[name] = value;
}

namespace {
struct ToJson : boost::static_visitor<> {
    ToJson ( Json::Value & arg ) : arg_ ( arg ) {}

    template<typename T>
    void operator() ( T & value ) const {
        arg_ = value;
    }
    private:
        Json::Value & arg_;
};
}

string Message::toJson()
{      
    using boost::apply_visitor;
    
    Json::Value obj;
    obj["method"] = Json::Value ( name_ );

    Json::Value args;

    for ( const std::pair<argument::Name, argument::Value> & arg : argMap_ ) {
        apply_visitor ( ToJson ( args[arg.first] ), arg.second );
    }

    obj["args"] = args;

    Json::FastWriter writer;
    return writer.write ( obj );
}
}

// kate: indent-mode cstyle; replace-tabs on; 
