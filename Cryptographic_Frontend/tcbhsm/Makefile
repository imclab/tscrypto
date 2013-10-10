include ../Makefile.inc
 

OBJS = Configuration.o Application.o main.o Session.o SessionObject.o Slot.o TcbError.o Token.o	
PKGCONFIG_LIBS = botan-1.10

PKGCONFIG_CFLAGS = $(shell pkg-config --cflags $(PKGCONFIG_LIBS))

CXXFLAGS = -I. -I.. -I../jsoncpp -Icryptoki/ $(PRJCXXFLAGS) $(PKGCONFIG_CFLAGS)

all: $(OBJS)

clean:
	-$(RM) -f $(OBJS)
