cmake_minimum_required ( VERSION 2.8 ) 

project ( cryptographic_frontend )

if(UNIX)
    SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -std=c++11 -g")
endif()

add_subdirectory ( src )
#add_subdirectory ( rabbitmq-c )
