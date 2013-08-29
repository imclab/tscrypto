CC  = @echo "[CC] $@."; clang
CXX = @echo "[CXX] $@."; clang++
LD  = @echo "[LD] $@."; clang++
AR  = @echo "[AR] $@."; ar
RANLIB  = @echo "[RANLIB] $@."; ranlib
RM  = rm
ECHO  = echo
SHELL = /bin/bash

PRJCFLAGS = -Wall -ggdb -pipe -fpic
PRJCXXFLAGS = -Wall -ggdb -std=c++11 -pipe -fpic
PRJLDFLAGS = -Wall -pipe -fpic
ARFLAGS =