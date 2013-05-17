/**
 * @file 
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */ 

#ifndef GenerateKeyPairMethod_H_
#define GenerateKeyPairMethod_H_

#include "Method.h"
#include <string>

namespace cf {

  class GenerateKeyPairMethod : public Method {
    public: 
        GenerateKeyPairMethod(std::string label, std::string id, std::string keyType, int keySize);
  }; 

} 

#endif // GenerateKeyPairMethod_H_
