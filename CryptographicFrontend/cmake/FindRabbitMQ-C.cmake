# @autor Francisco Cifuentes <francisco@niclabs.cl>

# - Try to find RabbitMQ-C
# Once done this will define
#  RABBITMQ-C_FOUND - System has RabbitMQ-C
#  RABBITMQ-C_INCLUDE_DIRS - The RabbitMQ-C include directories
#  RABBITMQ-C_LIBRARIES - The libraries needed to use RabbitMQ-C
#  RABBITMQ-C_DEFINITIONS - Compiler switches required for using RabbitMQ-C

find_package(PkgConfig)
pkg_check_modules(PC_RABBITMQ-C QUIET librabbitmq)
set(RABBITMQ-C_DEFINITIONS ${PC_RABBITMQ-C_CFLAGS_OTHER})

find_path(RABBITMQ-C_INCLUDE_DIR amqp.h
          HINTS ${PC_RABBITMQ-C_INCLUDEDIR} ${PC_RABBITMQ-C_INCLUDE_DIRS}
          PATH_SUFFIXES rabbitmq-c )

find_library(RABBITMQ-C_LIBRARY NAMES librabbitmq.so
             HINTS ${PC_RABBITMQ-C_LIBDIR} ${PC_RABBITMQ-C_LIBRARY_DIRS} )

set(RABBITMQ-C_LIBRARIES ${RABBITMQ-C_LIBRARY} )
set(RABBITMQ-C_INCLUDE_DIRS ${RABBITMQ-C_INCLUDE_DIR} )

include(FindPackageHandleStandardArgs)
# handle the QUIETLY and REQUIRED arguments and set RABBITMQ-C_FOUND to TRUE
# if all listed variables are TRUE
find_package_handle_standard_args(RabbitMQ-C  DEFAULT_MSG
                                  RABBITMQ-C_LIBRARY RABBITMQ-C_INCLUDE_DIR)
mark_as_advanced(RABBITMQ-C_INCLUDE_DIR RABBITMQ-C_LIBRARY )
