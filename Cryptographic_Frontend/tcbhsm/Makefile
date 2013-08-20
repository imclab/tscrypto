include ../Makefile.inc
 

OBJS = Application.o main.o Session.o SessionObject.o Slot.o TcbError.o Token.o	

CXXFLAGS = -I. -I.. -Icryptoki/ $(PRJCXXFLAGS) $(INCLUDES)

all: $(OBJS)

clean:
	-$(RM) -f $(OBJS)
