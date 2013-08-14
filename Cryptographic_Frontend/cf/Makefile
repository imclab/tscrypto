include ../Makefile.inc

INCLUDES = -I.

CXXFLAGS	=	$(PRJCXXFLAGS) $(INCLUDES)
OBJLIBS	=	../libcf.a
OBJS	=	Argument.o FindKeyMethod.o GenerateKeyPairMethod.o GetAttributeMethod.o IntegerArgument.o jsoncpp.o Method.o MethodMessage.o RabbitConnection.o ResponseMessage.o SignInitMethod.o SignMethod.o StringArgument.o

all:	$(OBJLIBS)

$(OBJLIBS):	$(OBJS)
	$(AR) $(ARFLAGS) rv $@ $?
	$(RANLIB) $@

clean:
	-$(RM) -f $(OBJS)
