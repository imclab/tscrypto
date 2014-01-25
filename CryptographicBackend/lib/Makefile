JAVA_PATH=/opt/java

INCLUDE=-I$(JAVA_PATH)/include/ -I$(JAVA_PATH)/include/linux/ `pkg-config --cflags libcrypto`
LIBS=`pkg-config --libs libcrypto`

CC=gcc
LD=gcc

CFLAGS=-fPIC -O2 $(INCLUDE)
LDFLAGS=$(LIBS)

all: libjniSignWrapper.so

libjniSignWrapper.so: JniSignWrapper.o
	$(LD) -shared $< -o $@ $(LDFLAGS)

JniSignWrapper.o: JniSignWrapper.c JniSignWrapper.h

clean:
	rm *.o *.so
