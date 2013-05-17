set (src_dir ${cryptographic_frontend_SOURCE_DIR})
include_directories (${src_dir}/json/include ${src_dir}/lib/rabbitmq/include)
link_directories (${src_dir}/lib/rabbitmq/lib)

file (GLOB SOURCES *.cpp *.h)

add_executable (cryptographic_frontend ${SOURCES})

target_link_libraries (cryptographic_frontend rabbitmq)
set_target_properties (cryptographic_frontend PROPERTIES RUNTIME_OUTPUT_DIRECTORY ${src_dir}/build)
