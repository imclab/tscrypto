#ifndef ARGUMENT_H_
#define ARGUMENT_H_

#include <string>

namespace cf {

enum class ArgumentType { String, Integer };
struct ArgumentValue {
private:
  std::string s;
  union {
    int i;
  };
public:
  ArgumentValue(std::string _s) : s(_s){}
  ArgumentValue(int _i) : i(_i){}
  operator std::string() { return s; }
  operator int() {return i;}
};

class Argument {
  protected:
    std::string name_;

  public:
    Argument(std::string name_);
    virtual ~Argument();
    virtual std::string getName();
    virtual ArgumentType type() const = 0;
    virtual ArgumentValue value() = 0;
};

}

#endif
