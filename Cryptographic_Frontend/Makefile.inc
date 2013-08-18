CC  = @echo "[CC] $@."; gcc
CXX = @echo "[CXX] $@."; g++
PRJCFLAGS = -ggdb -pipe
PRJCXXFLAGS = -ggdb -std=c++11 -pipe
PRJLDFLAGS = -pipe
LD  = @echo "[LD] $@."; g++
AR  = @echo "[AR] $@."; ar
ARFLAGS =
RANLIB  = @echo "[RANLIB] $@."; ranlib
RM  = rm
ECHO  = echo

SHELL = /bin/bash

