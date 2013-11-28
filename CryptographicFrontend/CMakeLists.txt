cmake_minimum_required(VERSION 2.6)
project (CryptographicFrontend)

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${CMAKE_CURRENT_SOURCE_DIR}/cmake)
SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

include(FindRabbitMQ-C)

set(Boost_USE_STATIC_LIBS OFF)
set(Boost_USE_MULTITHREADED ON)
set(Boost_USE_STATIC_RUNTIME OFF)

find_package(Boost 1.54 REQUIRED)
find_package(RabbitMQ-C)

if (Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
else()
    message(FATAL_ERROR "Cannot find boost library")
endif()

if (RABBITMQ-C_FOUND)
    include_directories(${RABBITMQ-C_INCLUDE_DIRS})
  else()
    message(FATAL_ERROR "Cannot find rabbitmq-c library")
endif()

include_directories("cryptoki")
include_directories("base64")
include_directories("jsoncpp")
include_directories("communication")
include_directories("hsm")

get_cmake_property(_variableNames VARIABLES)
foreach (_variableName ${_variableNames})
    message(STATUS "${_variableName}=${${_variableName}}")   
endforeach()

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
  communication/MethodMessage.cpp
  communication/MethodMessage.h
  communication/OpenSessionMethod.cpp
  communication/OpenSessionMethod.h
  communication/RabbitConnection.cpp
  communication/RabbitConnection.h
  communication/ResponseMessage.cpp
  communication/ResponseMessage.h
  communication/SeedRandomMethod.cpp
  communication/SeedRandomMethod.h
  communication/SignInitMethod.cpp
  communication/SignInitMethod.h
  communication/SignMethod.cpp
  communication/SignMethod.h
  communication/Argument.h
  communication/CloseSessionMethod.cpp
  communication/CloseSessionMethod.h
  communication/Connection.h
  communication/DeleteKeyPairMethod.cpp
  communication/DeleteKeyPairMethod.h
  communication/DigestInitMethod.cpp
  communication/DigestInitMethod.h
  communication/DigestMethod.cpp
  communication/DigestMethod.h
  communication/FindKeyMethod.cpp
  communication/FindKeyMethod.h
  communication/GenerateKeyPairMethod.cpp
  communication/GenerateKeyPairMethod.h
  communication/GenerateRandomMethod.cpp
  communication/GenerateRandomMethod.h
  communication/GetAttributeMethod.cpp
  communication/GetAttributeMethod.h
  )  
  
set(HSM_SRC 
  hsm/Session.cpp
  hsm/Session.h
  hsm/Slot.cpp
  hsm/Slot.h
  hsm/TcbError.cpp
  hsm/TcbError.h
  hsm/Token.cpp
  hsm/Token.h
  hsm/TokenSerializer.h
  hsm/config.h
  hsm/Configuration.cpp
  hsm/Configuration.h
  hsm/ConnectionManager.cpp
  hsm/ConnectionManager.h
  hsm/CryptoObject.cpp
  hsm/CryptoObject.h
  hsm/Database.h
  hsm/Application.cpp
  hsm/Application.h
  )
  
  
add_library(pkcs11 SHARED pkcs11.cpp ${BASE64_SRC} ${JSONCPP_SRC} ${COMMUNICATION_SRC} ${HSM_SRC})
# target_link_libraries(pkcs11 ${RABBITMQ-C_LIBRARIES})