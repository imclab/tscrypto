include Makefile.inc

DIRS	=	cf tcbhsm
EXE	=	hsm
OBJS	=	main.o
OBJLIBS	=	libcf.a libtcbhsm.a
LDFLAGS	=	-L. -lcf -ltcbhsm -lrabbitmq
CXXFLAGS=	$(PRJCXXFLAGS)

all:	$(EXE)

$(EXE):	$(OBJLIBS) $(OBJS) 
	$(LD) -o $@ $(OBJS) $(OBJLIB) $(LDFLAGS)

libcf.a: force_look
	@cd cf; $(MAKE) $(MFLAGS)

libtcbhsm.a: force_look
	@cd tcbhsm; $(MAKE) $(MFLAGS)

clean:
	-$(RM) -f $(EXE) $(OBJS) $(OBJLIBS)
	-for dir in $(DIRS); do (cd $$dir; $(MAKE) clean); done

force_look:
	@true
