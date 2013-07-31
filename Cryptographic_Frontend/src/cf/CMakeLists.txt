set (src_dir ${cryptographic_frontend_SOURCE_DIR})

find_package (RabbitMQ REQUIRED)

include_directories (${src_dir}/json/include ${src_dir}/boost/include ${RABBITMQ_INCLUDE_DIR})
link_directories (${RABBITMQ_LIBRARIES})

set (SOURCES Argument.cpp GenerateKeyPairMethod.cpp  
IntegerArgument.cpp MethodMessage.cpp SignInitMethod.cpp
StringArgument.cpp FindKeyMethod.cpp GetAttributeMethod.cpp 
jsoncpp.cpp Method.cpp RabbitConnection.cpp 
SignMethod.cpp ResponseMessage.cpp)

add_library (cryptographic_frontend SHARED ${SOURCES})
target_link_libraries (cryptographic_frontend ${RABBITMQ_LIBRARIES})
set_target_properties (cryptographic_frontend PROPERTIES LIBRARY_OUTPUT_DIRECTORY ${src_dir}/lib)

