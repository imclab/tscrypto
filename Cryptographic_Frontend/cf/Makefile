include ../Makefile.inc

PKGCONFIG_LIBS    = librabbitmq
OBJS	= FindKeyMethod.o GenerateKeyPairMethod.o GetAttributeMethod.o Method.o MethodMessage.o RabbitConnection.o ResponseMessage.o SignInitMethod.o SignMethod.o DeleteKeyPairMethod.o
CXXFLAGS= -I../jsoncpp/ $(PRJCXXFLAGS)


all:	$(OBJS)

clean:
	-$(RM) -f $(OBJS)
