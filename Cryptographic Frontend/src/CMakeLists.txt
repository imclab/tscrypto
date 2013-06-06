set (src_dir ${cryptographic_frontend_SOURCE_DIR})
#include_directories (${src_dir}/json/include ${src_dir}/lib/rabbitmq/include ${src_dir}/boost/include ${src_dir}/src)

find_package (RabbitMQ REQUIRED)

include_directories (${src_dir}/json/include ${src_dir}/boost/include ${RABBITMQ_INCLUDE_DIR} ${src_dir}/include)
link_directories (${RABBITMQ_LIBRARIES})

set (SOURCES Argument.cpp 
methods/GenerateKeyPairMethod.cpp  IntegerArgument.cpp  
MethodMessage.cpp methods/SignInitMethod.cpp 
StringArgument.cpp methods/FindKeyMethod.cpp 
methods/GetAttributeMethod.cpp jsoncpp.cpp Method.cpp 
RabbitConnection.cpp methods/SignMethod.cpp ResponseMessage.cpp)

add_library (cryptographic_frontend SHARED ${SOURCES})
target_link_libraries (cryptographic_frontend ${RABBITMQ_LIBRARIES})
set_target_properties (cryptographic_frontend PROPERTIES LIBRARY_OUTPUT_DIRECTORY ${src_dir}/lib)

add_executable (cf main.cpp)
target_link_libraries (cf cryptographic_frontend)
set_target_properties (cf PROPERTIES RUNTIME_OUTPUT_DIRECTORY ${src_dir})
