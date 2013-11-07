CC  = @echo "[CC] $@."; gcc
CXX = @echo "[CXX] $@."; g++
LD  = @echo "[LD] $@."; g++
AR  = @echo "[AR] $@."; ar
RANLIB  = @echo "[RANLIB] $@."; ranlib
RM  = rm
ECHO  = echo
SHELL = /bin/bash

PRJCFLAGS = -Wall -ggdb -pipe -fpic -pg
PRJCXXFLAGS = -Wall -ggdb -std=c++11 -pipe -fpic -pg
PRJLDFLAGS = -Wall -pipe -fpic
ARFLAGS =