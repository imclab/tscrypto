include ../Makefile.inc
 

OBJS = Configuration.o Application.o hsm.o CryptoObject.o Slot.o TcbError.o Token.o ConnectionManager.o Session.o
CXXFLAGS = -I. -I.. -I../jsoncpp -Icryptoki/ $(PRJCXXFLAGS)

all: $(OBJS)

clean:
	-$(RM) -f $(OBJS)
