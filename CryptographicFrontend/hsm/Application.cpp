/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "Application.h"
#include "Configuration.h"
#include "CryptoObject.h"
#include "Database.h"
#include "Session.h"
#include "Slot.h"
#include "Token.h"
#include "TcbError.h"
#include "ConnectionManager.h"

#include <base64/base64.h>

#include <cstdlib> // getenv
#include <ostream>
#include <iostream>

using namespace hsm;


Application::Application(std::ostream& out)
: out_(out)
{ 
  // First, read and setup the configuration.
  char const * configPath = std::getenv("TCB_CONFIG_FILE");
  if (configPath == nullptr) {
    throw TcbError("Application::Application", 
                   "TCB_CONFIG_FILE environment variable hasn't setted yet", 
                   CKR_DEVICE_ERROR);    
  }  
  configuration_.reset(new Configuration(std::string(configPath)));
  
  // By design, we will have one slot per configured token.  
  // The tokens are owned by the slots.
  int i = 0;
  for (auto const & slotConf: configuration_->getSlotConf()) {
    SlotPtr slot(new Slot(i));    
    
    slot->insertToken(new Token(slotConf.label, slotConf.userPin, slotConf.soPin, *slot));
    
    slots_.push_back(std::move(slot));    
  }  
  
  connectionManager_.reset(new ConnectionManager(*configuration_));
  
}

namespace hsm {
class DebugDatabase : public Database {
public:
  virtual void saveToken(Token & token) override {
    std::cout << "{ \"label\" : \"" << token.getLabel() << "\", \"objects\" : [ ";
    for(auto const& objectPair: token.getObjects()) {
      // objectPair : std::pair<CK_OBJECT_HANDLE, CryptoObjectPtr>
      std::cout << "{ \"handle\" : " << objectPair.first << " , ";
      std::cout << " \"attributes\" : [ ";
      for(auto const& attr: objectPair.second->getAttributes()) {              
        std::cout << "{ \"type\" : " << attr.type << " ,";
        std::cout << " \"value\" : \"";              
        std::cout << base64::encode((unsigned char*) attr.pValue, attr.ulValueLen);
        std::cout << "\"}, ";
      }
      std::cout << " ] }, ";            
    }
    std::cout << " ] }" << std::endl;
      
  }
  
  virtual TokenPtr getToken(std::string const & label) override {
    TokenPtr p;
    return p;
  }
};
}

Application::~Application() {
  DebugDatabase db;
  for(auto const& slotPtr: slots_) {
    db.saveToken(slotPtr->getToken());
  }
}

void Application::errorLog(std::string message) const
{
  out_ << message << std::endl;
}

Session & Application::getSession(CK_SESSION_HANDLE session)
{
  return getSessionSlot(session).getSession(session);
}

const std::vector<SlotPtr> & Application::getSlotList() const
{
  return slots_;
}

Slot & Application::getSlot(CK_SLOT_ID id) const
{
  unsigned int i = static_cast<unsigned int>(id);
  try {
    Slot &slot = *(slots_.at(i));
    return slot;
  } 
  catch (std::out_of_range &e) {
    throw TcbError("Application::getSlot", e.what(), CKR_SLOT_ID_INVALID);
  }
}

Slot & Application::getSessionSlot(CK_SESSION_HANDLE handle) {
  for(auto & slotPtr: slots_) {
    if (slotPtr->hasSession(handle)) {
      return *slotPtr;
    }
  }
  
  throw TcbError("Application::getSessionSlot", 
                 "Session not found.", 
                 CKR_SESSION_HANDLE_INVALID);
}

Configuration const & Application::getConfiguration() const { // throws exception
  return *configuration_;
}

ConnectionManager const & Application::getConnectionManager() const
{
  return *connectionManager_;
}
