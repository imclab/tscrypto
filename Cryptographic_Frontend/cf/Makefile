include ../Makefile.inc

PKGCONFIG_LIBS    = librabbitmq
OBJS	= Argument.o FindKeyMethod.o GenerateKeyPairMethod.o GetAttributeMethod.o IntegerArgument.o jsoncpp.o Method.o MethodMessage.o RabbitConnection.o ResponseMessage.o SignInitMethod.o SignMethod.o StringArgument.o
CXXFLAGS= -I. $(shell pkg-config --libs $(PKGCONFIG_LIBS)) $(PRJCXXFLAGS)


all:	$(OBJS)

clean:
	-$(RM) -f $(OBJS)
