/**
 * @author Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "tcbhsm/Session.h"
#include "cf/RabbitConnection.hpp"

using namespace tcbhsm;

Session::Session()
{

}

Session::~Session()
{

}

long long Session::getKeyHandle(CK_OBJECT_HANDLE hKey)
{
  // TODO: crear sistema de persistencia de datos con SQLite
    return 0;
}

Connection* Session::createConnection() {
  // TODO: crear algun tipo de sistema de configuración (Me tinca algo como
  // variables de entorno)...
  return new RabbitConnection("localhost", 5672, "", "rpc_queue", 1);
}
