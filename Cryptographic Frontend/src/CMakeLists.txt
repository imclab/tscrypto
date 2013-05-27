set (src_dir ${cryptographic_frontend_SOURCE_DIR})
include_directories (${src_dir}/json/include ${src_dir}/lib/rabbitmq/include ${src_dir}/boost/include
${src_dir}/src)
link_directories (${src_dir}/lib/rabbitmq/lib)

set (SOURCES Argument.hpp Connection.hpp
methods/FindKeyMethod.hpp methods/GenerateKeyPairMethod.hpp 
methods/GetAttributeMethod.hpp IntegerArgument.hpp 
Method.hpp MethodMessage.hpp RabbitConnection.hpp 
methods/SignInitMethod.hpp methods/SignMethod.hpp StringArgument.hpp
ResponseMessage.hpp Argument.cpp 
methods/GenerateKeyPairMethod.cpp  IntegerArgument.cpp  
MethodMessage.cpp methods/SignInitMethod.cpp 
StringArgument.cpp methods/FindKeyMethod.cpp 
methods/GetAttributeMethod.cpp jsoncpp.cpp Method.cpp 
RabbitConnection.cpp methods/SignMethod.cpp ResponseMessage.cpp)


add_library (cryptographic_frontend SHARED ${SOURCES})
target_link_libraries (cryptographic_frontend rabbitmq)
set_target_properties (cryptographic_frontend PROPERTIES RUNTIME_OUTPUT_DIRECTORY ${src_dir}/build)

add_executable (cf main.cpp)
target_link_libraries (cf cryptographic_frontend)
set_target_properties (cf PROPERTIES RUNTIME_OUTPUT_DIRECTORY ${src_dir}/build)
