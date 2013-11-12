#include <fstream>
#include <cerrno>
#include <json/json.h>

#include "Configuration.h"
#include "TcbError.h"
#include "cryptoki.h"

using namespace hsm;

namespace
{

std::string getFileContents ( const char *filename )
{
    std::ifstream in ( filename, std::ios::in | std::ios::binary );
    if ( in ) {
        std::string contents;
        in.seekg ( 0, std::ios::end );
        contents.resize ( in.tellg() );
        in.seekg ( 0, std::ios::beg );
        in.read ( &contents[0], contents.size() );
        in.close();
        return ( contents );
    }
    throw TcbError ( "getFileContents", CKR_GENERAL_ERROR );
}
}

Configuration::Configuration ( std::string configurationPath )
{
    std::string configJson = getFileContents ( configurationPath.c_str() );

    Json::Value root;
    Json::Reader reader;
    if ( !reader.parse ( configJson, root ) ) {
        throw TcbError ( "Configuration::Configuration", "Cannot parse config file", CKR_GENERAL_ERROR );
    }

    const Json::Value rabbitmq = root["rabbitmq"];
    rabbitMqConf_.hostname = rabbitmq["hostname"].asString();
    rabbitMqConf_.port = rabbitmq["port"].asString();
    rabbitMqConf_.rpcQueue = rabbitmq["rpc_queue"].asString();

    const Json::Value database = root["database"];
    databaseConf_.path = database["path"].asString();

    const Json::Value slots = root["slots"];
    for ( unsigned int i=0; i < slots.size(); ++i ) {
        const Json::Value token = slots[i];
        slotConf_.push_back ( {
            token["label"].asString(),
            token["user_pin"].asString(),
            token["so_pin"].asString()

        } );
    }

}

Configuration::RabbitMqConf const & Configuration::getRabbitMqConf() const
{
    return rabbitMqConf_;
}

std::vector<Configuration::SlotConf> const & Configuration::getSlotConf() const
{
    return slotConf_;
}

Configuration::DatabaseConf const & Configuration::getDatabaseConf() const
{
    return databaseConf_;
}
