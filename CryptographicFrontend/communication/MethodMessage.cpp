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

using namespace communication;

MethodMessage::MethodMessage(const string & name)
{
  name_ = name;
}

MethodMessage::~MethodMessage() {}

void MethodMessage::addArgument(IArgument* arg)
{
  argList_.push_back(ArgumentPtr(arg));
}

string MethodMessage::toJson()
{
  Json::Value obj;
  obj["method"] = Json::Value(name_);

  Json::Value args;

  for (const ArgumentPtr & arg : argList_) {
      if (IntegerArgument::match(*arg)) {
          args[arg->getName()] = IntegerArgument::getValue(*arg);
      } else if (StringArgument::match(*arg)) {
          args[arg->getName()] = StringArgument::getValue(*arg);
      }
  }

  obj["args"] = args;

  Json::FastWriter writer;
  return writer.write(obj);
}

// kate: indent-mode cstyle; indent-width 4; replace-tabs on;
