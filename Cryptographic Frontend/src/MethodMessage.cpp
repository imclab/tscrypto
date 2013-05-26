#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <json/json.h>
#include <MethodMessage.hpp>

using std::string;
using std::vector;
using std::stringstream;

namespace cf
{

MethodMessage::MethodMessage(string name)
{
    name_ = name;
}

MethodMessage::~MethodMessage() {}

void MethodMessage::addArgument(ArgumentPtr arg)
{
    argList_.push_back(std::move(arg));
}

string MethodMessage::toJson()
{
    Json::Value obj;
    obj["method"] = Json::Value(name_);

    Json::Value args;

    for (auto const & arg : argList_) {
        if (arg->type() == ArgumentType::Integer)
            args[arg->getName()] = static_cast<int>(*arg);
        else if (arg->type() == ArgumentType::String)
            args[arg->getName()] = static_cast<std::string>(*arg);
    }

    obj["args"] = args;

    return obj.toStyledString();
}

}

