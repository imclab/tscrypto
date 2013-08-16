include ../Makefile.inc

# INCLUDES = -I.

PKGCONFIG_LIBS    = librabbitmq
OBJLIBS	= ../libcf.a
OBJS	= Argument.o FindKeyMethod.o GenerateKeyPairMethod.o GetAttributeMethod.o IntegerArgument.o jsoncpp.o Method.o MethodMessage.o RabbitConnection.o ResponseMessage.o SignInitMethod.o SignMethod.o StringArgument.o

CXXFLAGS= -I. $(shell pkg-config --libs $(PKGCONFIG_LIBS)) $(PRJCXXFLAGS)

all:	$(OBJLIBS)

$(OBJLIBS):	$(OBJS)
	$(AR) $(ARFLAGS) rv $@ $?
	$(RANLIB) $@

clean:
	-$(RM) -f $(OBJS)
