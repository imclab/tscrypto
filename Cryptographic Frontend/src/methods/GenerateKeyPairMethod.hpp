/**
 * @file 
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */ 

#ifndef GenerateKeyPairMethod_HPP
#define GenerateKeyPairMethod_HPP

#include <Method.hpp>

#include <string>
#include <memory>

namespace cf {
  
  class GenerateKeyPairMethod : public Method {
    
  protected:
    virtual ResponseMessageStrategy getStrategy() const override;
    
  public: 
    GenerateKeyPairMethod(std::string label, std::string id, std::string keyType, int keySize);
    
  }; 
  
} 

#endif // GenerateKeyPairMethod_H_
