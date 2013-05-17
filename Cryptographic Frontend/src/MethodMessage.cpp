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
  this->name = name;
}

MethodMessage::~MethodMessage() {
  for (Argument* arg : argList) {
    delete arg;
  }
}

void MethodMessage::addArgument(Argument* arg) {
  argList.push_back(arg);
}

string MethodMessage::toJson() {
  Json::Value obj;
  obj["method"] = Json::Value(this->name);
  
  Json::Value args;
  
  for (auto arg: argList) {
    if (arg->type() == ArgumentType::Integer) {
      args[arg->getName()] = arg->value().i;
    } else if (arg->type() == ArgumentType::String) {
      args[arg->getName()] = arg->value().s;
    }
  }
  
  obj["args"] = args;

  return obj.toStyledString();
}

}

