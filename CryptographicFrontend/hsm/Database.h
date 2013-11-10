#ifndef TCBHSM_DATABASE_H
#define TCBHSM_DATABASE_H

#include <memory>
#include <vector>

namespace hsm {
  class Token;
  using TokenPtr = std::unique_ptr<Token>;
  
  class Database {
  public:
    virtual ~Database() = default;
    virtual void saveToken(Token & token) = 0;
    virtual TokenPtr getToken(std::string const & label) = 0;
  };
}

#endif // TCBHSM_DATABASE_H