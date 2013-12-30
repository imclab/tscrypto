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

# - Try to find Sqlite3
# Once done this will define
#  SQLITE3_FOUND - System has Sqlite3
#  SQLITE3_INCLUDE_DIRS - The Sqlite3 include directories
#  SQLITE3_LIBRARIES - The libraries needed to use Sqlite3
#  SQLITE3_DEFINITIONS - Compiler switches required for using Sqlite3

find_package(PkgConfig)
pkg_check_modules(PC_SQLITE3 QUIET sqlite3)
set(SQLITE3_DEFINITIONS ${PC_SQLITE3_CFLAGS_OTHER})

find_path(SQLITE3_INCLUDE_DIR sqlite3.h
          HINTS ${PC_SQLITE3_INCLUDEDIR} ${PC_SQLITE3_INCLUDE_DIRS}
          PATH_SUFFIXES sqlite3 )

find_library(SQLITE3_LIBRARY NAMES libsqlite3.so
             HINTS ${PC_SQLITE3_LIBDIR} ${PC_SQLITE3_LIBRARY_DIRS} )

set(SQLITE3_LIBRARIES ${SQLITE3_LIBRARY} )
set(SQLITE3_INCLUDE_DIRS ${SQLITE3_INCLUDE_DIR} )

include(FindPackageHandleStandardArgs)
# handle the QUIETLY and REQUIRED arguments and set SQLITE3_FOUND to TRUE
# if all listed variables are TRUE
find_package_handle_standard_args(Sqlite3  DEFAULT_MSG
                                  SQLITE3_LIBRARY SQLITE3_INCLUDE_DIR)
mark_as_advanced(SQLITE3_INCLUDE_DIR SQLITE3_LIBRARY )
