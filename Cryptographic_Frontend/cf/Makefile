include ../Makefile.inc

PKGCONFIG_LIBS    = librabbitmq
OBJS	= Argument.o FindKeyMethod.o GenerateKeyPairMethod.o GetAttributeMethod.o Method.o MethodMessage.o RabbitConnection.o ResponseMessage.o SignInitMethod.o SignMethod.o
CXXFLAGS= -I../jsoncpp/ $(PRJCXXFLAGS)


all:	$(OBJS)

clean:
	-$(RM) -f $(OBJS)
