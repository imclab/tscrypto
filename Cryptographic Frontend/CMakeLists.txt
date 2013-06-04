cmake_minimum_required ( VERSION 2.8 ) 
project ( cryptographic_frontend )

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${CMAKE_CURRENT_SOURCE_DIR}/cmake)

#if(UNIX)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -std=c++11 -g")
#endif()

add_subdirectory ( src )
#add_subdirectory ( rabbitmq-c )
#add_subdirectory(boost/include)
