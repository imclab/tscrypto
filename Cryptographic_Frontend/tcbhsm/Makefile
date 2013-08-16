include ../Makefile.inc
 
OBJLIBS = ../libtcbhsm.a
OBJS = Application.o main.o Session.o SessionObject.o Slot.o TcbError.o Token.o	

CXXFLAGS = -I. -I.. -Icryptoki/ $(PRJCXXFLAGS) $(INCLUDES)
all: $(OBJLIBS)

$(OBJLIBS): $(OBJS)
	$(AR) $(ARFLAGS) rv $@ $?
	$(RANLIB) $@

clean:
	-$(RM) -f $(OBJS)
