add_subdirectory(cf) # Primero la librería
add_subdirectory(tcbhsm)
set (src_dir ${cryptographic_frontend_SOURCE_DIR})

include_directories(${src_dir}/src)
add_executable(main_test main.cpp)
target_link_libraries(main_test cryptographic_frontend)