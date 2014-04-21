cmake_minimum_required(VERSION 2.6)
project (CryptographicFrontend)

set(CryptographicFrontend_VERSION_MAJOR 1)
set(CryptographicFrontend_VERSION_MINOR 0)
set(MAX_SESSION_COUNT 5)
set(MIN_PIN_LEN 3)
set(MAX_PIN_LEN 10)

configure_file (
  "${PROJECT_SOURCE_DIR}/hsm/config.h.in"
  "${PROJECT_BINARY_DIR}/hsm/config.h"
  )
  
include_directories("${PROJECT_BINARY_DIR}/hsm")  
  

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${CMAKE_CURRENT_SOURCE_DIR}/cmake)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

include(FindSqlite3)
include(FindZeroMQ)

set(Boost_USE_STATIC_LIBS OFF)
set(Boost_USE_MULTITHREADED ON)
set(Boost_USE_STATIC_RUNTIME OFF)

find_package(Boost 1.53 REQUIRED)
find_package(Sqlite3)
find_package(ZeroMQ)

if (Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
else()
    message(FATAL_ERROR "Cannot find boost library")
endif()

if (SQLITE3_FOUND)
    include_directories(${SQLITE3_INCLUDE_DIRS})
  else()
    message(FATAL_ERROR "Cannot find sqlite3 library")
endif()

if (ZEROMQ_FOUND)
    include_directories(${ZEROMQ_INCLUDE_DIRS})
  else()
    message(FATAL_ERROR "Cannot find zeromq library")
endif()

include_directories("cryptoki_compat")
include_directories("base64")
include_directories("jsoncpp")
include_directories("communication")
include_directories("hsm")

set(BASE64_SRC 
  base64/base64.cpp
  base64/base64.h
  )

set(JSONCPP_SRC 
  jsoncpp/jsoncpp.cpp
  )
  
set(COMMUNICATION_SRC
  communication/Method.cpp
  communication/Method.h
  communication/MethodFactory.cpp
  communication/MethodFactory.h
  communication/MethodMessage.cpp
  communication/MethodMessage.h
  communication/ResponseMessage.h
  communication/ResponseMessage.cpp
  communication/Argument.h
  communication/RPC.h
  communication/ZeroConnection.cpp
  )  
  
set(HSM_SRC   
  hsm/Database.cpp 
  hsm/Database.h
  hsm/Session.cpp
  hsm/Session.h
  hsm/Slot.cpp
  hsm/Slot.h
  hsm/TcbError.cpp
  hsm/TcbError.h
  hsm/Token.cpp
  hsm/Token.h
  hsm/TokenSerializer.h
  hsm/Configuration.cpp
  hsm/Configuration.h
  hsm/ConnectionManager.h 
  hsm/CryptoObject.cpp
  hsm/CryptoObject.h
  hsm/Application.cpp
  hsm/Application.h
  )
  
  
add_library(pkcs11 SHARED pkcs11.cpp ${BASE64_SRC} ${JSONCPP_SRC} ${COMMUNICATION_SRC} ${HSM_SRC})
target_link_libraries(pkcs11 ${RABBITMQ-C_LIBRARIES} ${SQLITE3_LIBRARIES} ${ZEROMQ_LIBRARIES})

add_executable(test test.c)
target_link_libraries(test pkcs11)
