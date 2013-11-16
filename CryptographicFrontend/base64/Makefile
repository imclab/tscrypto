include ../Makefile.inc

OBJS	= base64.o
CXXFLAGS= $(PRJCXXFLAGS)

all:	$(OBJS)

clean:
	-$(RM) -f $(OBJS)

%.d: %.cpp
	@set -e; rm -f $@; \
	$(CXX) -M $(CPPFLAGS) $< > $@.$$$$; \
	sed 's,\($*\)\.o[ :]*,\1.o $@ : ,g' < $@.$$$$ > $@; \
	rm -f $@.$$$$	