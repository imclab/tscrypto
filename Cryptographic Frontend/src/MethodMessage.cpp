#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <json/json.h>

// #include "Argument.h"
#include "MethodMessage.h"

using std::string;
using std::vector;
using std::stringstream;

namespace cf {

MethodMessage::MethodMessage(string name) {
  name_ = name;
}

MethodMessage::~MethodMessage() {}

void MethodMessage::addArgument(ArgumentPtr arg) {
  argList_.push_back(std::move(arg));
}

string MethodMessage::toJson() {
  Json::Value obj;
  obj["method"] = Json::Value(name_);
  
  Json::Value args;
  
  for (auto const& arg: argList_) {
    if (arg->type() == ArgumentType::Integer) {
      args[arg->getName()] = (int)(arg->value());
    } else if (arg->type() == ArgumentType::String) {
      args[arg->getName()] = (std::string)(arg->value());
    }
  }
  
  obj["args"] = args;

  return obj.toStyledString();
}

}

