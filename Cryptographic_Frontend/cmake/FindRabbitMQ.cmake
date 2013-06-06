FIND_PATH(RABBITMQ_INCLUDE_DIR
    NAMES amqp.h amqp_framing.h amqp_ssl_socket.h amqp_tcp_socket.h
    DOC "Path containing the rabbitmq-c includes files"
    )

FIND_LIBRARY(RABBITMQ_LIBRARIES
    NAMES rabbitmq
    DOC "rabbitmq library path"
    )

include(FindPackageHandleStandardArgs)

FIND_PACKAGE_HANDLE_STANDARD_ARGS(RABBITMQ
    REQUIRED_VARS RABBITMQ_INCLUDE_DIR RABBITMQ_LIBRARIES
  )
