include ../Makefile.inc
 

OBJS = Configuration.o Application.o main.o CryptoObject.o Slot.o TcbError.o Token.o ConnectionManager.o Session.o Database.cpp
CXXFLAGS = -I. -I.. -I../jsoncpp -Icryptoki/ $(PRJCXXFLAGS)

all: $(OBJS)

clean:
	-$(RM) -f $(OBJS)
