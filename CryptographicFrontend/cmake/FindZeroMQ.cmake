# <one line to give the library's name and an idea of what it does.>
# Copyright (C) 2014  Francisco Cifuentes <email>
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
# 

# - Try to find ZeroMQ
# Once done this will define
#  ZEROMQ_FOUND - System has ZeroMQ
#  ZEROMQ_INCLUDE_DIRS - The ZeroMQ include directories
#  ZEROMQ_LIBRARIES - The libraries needed to use ZeroMQ
#  ZEROMQ_DEFINITIONS - Compiler switches required for using ZeroMQ

find_package(PkgConfig)
pkg_check_modules(PC_ZEROMQ QUIET libzqm)
set(ZEROMQ_DEFINITIONS ${PC_ZEROMQ_CFLAGS_OTHER})

find_path(ZEROMQ_INCLUDE_DIR zmq.hpp
          HINTS ${PC_ZEROMQ_INCLUDEDIR} ${PC_ZEROMQ_INCLUDE_DIRS}
          PATH_SUFFIXES zeromq )

find_library(ZEROMQ_LIBRARY NAMES libzmq.so
             HINTS ${PC_ZEROMQ_LIBDIR} ${PC_ZEROMQ_LIBRARY_DIRS} )

set(ZEROMQ_LIBRARIES ${ZEROMQ_LIBRARY} )
set(ZEROMQ_INCLUDE_DIRS ${ZEROMQ_INCLUDE_DIR} )

include(FindPackageHandleStandardArgs)
# handle the QUIETLY and REQUIRED arguments and set ZEROMQ_FOUND to TRUE
# if all listed variables are TRUE
find_package_handle_standard_args(ZeroMQ  DEFAULT_MSG
                                  ZEROMQ_LIBRARY ZEROMQ_INCLUDE_DIR)
mark_as_advanced(ZEROMQ_INCLUDE_DIR ZEROMQ_LIBRARY )
