#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <json/json.h>
#include "MethodMessage.h"

using std::string;
using std::vector;
using std::stringstream;

namespace communication {

MethodMessage::MethodMessage ( const string & name )
{
    name_ = name;
}

void MethodMessage::addArgument ( argument::Name name, argument::Value value )
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

string MethodMessage::toJson()
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
