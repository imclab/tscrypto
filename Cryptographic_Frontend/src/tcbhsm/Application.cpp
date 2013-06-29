/*
 * @autor Francisco Cifuentes <francisco@niclabs.cl>
 */

#include "tcbhsm/Application.h"

#include <iostream>

using namespace tcbhsm;

Application::Application()
: out_(&std::cerr)
{

}

Application::~Application()
{

}

void Application::errorLog(std::string message) const
{
    (*out_) << message << std::endl;
}

const Session &Application::getSession(CK_SESSION_HANDLE session) const
{
   return sessions_[session];
}
