#ifndef COMMUNICATION_ARGUMENT_H
#define COMMUNICATION_ARGUMENT_H

#include <string>
#include <unordered_map>

#include <boost/variant/variant.hpp>
#include <boost/variant/get.hpp>

namespace communication
{

namespace argument
{
using Name = std::string;
using Value = boost::variant<int, std::string>;
using Map = std::unordered_map<argument::Name, argument::Value>;
using boost::get;
}

}

#endif
// kate: indent-mode cstyle; replace-tabs on; 
