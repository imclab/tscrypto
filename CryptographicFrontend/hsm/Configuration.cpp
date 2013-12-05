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

    ptree rabbitmq = root.get_child("rabbitmq");    
    rabbitMqConf_.hostname = rabbitmq.get<string>("hostname");
    rabbitMqConf_.port = rabbitmq.get<string>("port");
    rabbitMqConf_.rpcQueue = rabbitmq.get<string>("rpc_queue");

    ptree database = root.get_child("database");          
    databaseConf_.path = database.get<string>("path");

    ptree slots = root.get_child("slots");
    for (ptree::value_type & v: slots) {
	ptree & token = v.second;
	slotConf_.push_back({ token.get<string>("label") });
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
