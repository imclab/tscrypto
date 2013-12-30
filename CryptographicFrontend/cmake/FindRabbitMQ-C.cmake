# Copyright 2013 NIC Chile Research Labs.
# This file is part of PKCS11-TsCrypto.
# 
# PKCS11-TsCrypto is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# PKCS11-TsCrypto is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public License
# along with PKCS11-TsCrypto.  If not, see <http://www.gnu.org/licenses/>.

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
