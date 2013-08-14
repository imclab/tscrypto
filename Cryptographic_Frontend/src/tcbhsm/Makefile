include ../Makefile.inc

INCLUDES = -I. -I.. -Icryptoki/

CXXFLAGS	=	$(PRJCXXFLAGS) $(INCLUDES)
OBJLIBS	=	../libtcbhsm.a
OBJS	=	Application.o main.o Session.o SessionObject.o Slot.o TcbError.o Token.o	
all:	$(OBJLIBS)

$(OBJLIBS):	$(OBJS)
	$(AR) $(ARFLAGS) rv $@ $?
	$(RANLIB) $@

clean:
	-$(RM) -f $(OBJS)
