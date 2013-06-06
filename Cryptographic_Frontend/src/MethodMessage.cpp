#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <json/json.h>
#include <MethodMessage.hpp>
#include "StringArgument.hpp"
#include "IntegerArgument.hpp"

using std::string;
using std::vector;
using std::stringstream;

namespace cf
{

namespace
{
class ArgumentValueVisitor : public ArgumentVisitor
{
private:
    Json::Value &obj_;

public:
    ArgumentValueVisitor(Json::Value &obj) : obj_(obj) {}
    virtual ~ArgumentValueVisitor() {}

    virtual void visit(Argument &arg);
    virtual void visit(StringArgument &arg) override;
    virtual void visit(IntegerArgument &arg) override;
};

void ArgumentValueVisitor::visit(Argument &arg)
{
}

void ArgumentValueVisitor::visit(StringArgument &arg)
{
    obj_[arg.getName()] = arg.value();
}

void ArgumentValueVisitor::visit(IntegerArgument &arg)
{
    obj_[arg.getName()] = arg.value();
}

}

MethodMessage::MethodMessage(const string &name)
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
    ArgumentValueVisitor visitor(args);

    for (auto const & arg : argList_)
        arg->accept(visitor);

    obj["args"] = args;

    return obj.toStyledString();
}

}

// kate: indent-mode cstyle; indent-width 4; replace-tabs on; 
