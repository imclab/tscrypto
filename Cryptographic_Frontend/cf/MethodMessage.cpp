#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <json/json.h>
#include "MethodMessage.hpp"
#include "Argument.hpp"

using std::string;
using std::vector;
using std::stringstream;

namespace cf
{

MethodMessage::MethodMessage(const string & name)
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

  for (const ArgumentPtr & arg : argList_) {
      if (is(*arg, IntegerArgument::type)) {
          args[arg->getName()] = IntegerArgument::getValue(*arg);
      } else if (is(*arg, StringArgument::type)) {
          args[arg->getName()] = StringArgument::getValue(*arg);
      }
  }

  obj["args"] = args;

  Json::FastWriter writer;
  return writer.write(obj);
}

}

// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
