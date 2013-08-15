include Makefile.inc

DIRS	=	cf tcbhsm
EXE	=	hsm
OBJS	=	main.o
OBJLIBS	=	libcf.a libtcbhsm.a
TEST	=	test
TESTOBJS	=	test.o
LDFLAGS	=	-L. -lrabbitmq -lcf -ltcbhsm
CFLAGS	=	$(PRJCFLAGS) -Itcbhsm/cryptoki
CXXFLAGS=	$(PRJCXXFLAGS)

all:	$(EXE) $(TEST)

$(EXE):	$(OBJLIBS) $(OBJS) 
	$(LD) -o $@ $(OBJS) $(LDFLAGS)
	
$(TEST):	$(OBJLIBS) $(TESTOBJS)
	$(LD) -o $@ $(TESTOBJS) $(OBJLIBS) $(LDFLAGS)

libcf.a: force_look
	@cd cf; $(MAKE) $(MFLAGS)

libtcbhsm.a: force_look
	@cd tcbhsm; $(MAKE) $(MFLAGS)

clean:
	-$(RM) -f $(EXE) $(OBJS) $(OBJLIBS)
	-for dir in $(DIRS); do (cd $$dir; $(MAKE) clean); done

force_look:
	@true
