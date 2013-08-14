add_subdirectory(cryptoki)

set (src_dir ${cryptographic_frontend_SOURCE_DIR})

include_directories (${src_dir}/src/ ${src_dir}/src/tcbhsm/cryptoki)

set (SOURCES Application.cpp
    main.cpp Session.cpp  SessionObject.cpp
    Slot.cpp  Token.cpp TcbError.cpp)

add_library (tcbhsm SHARED ${SOURCES})
target_link_libraries (tcbhsm ${RABBITMQ_LIBRARIES})
set_target_properties (tcbhsm PROPERTIES LIBRARY_OUTPUT_DIRECTORY ${src_dir}/lib)

