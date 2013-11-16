include ../Makefile.inc

PKGCONFIG_LIBS    = librabbitmq
OBJS	= FindKeyMethod.o GenerateKeyPairMethod.o GetAttributeMethod.o Method.o MethodMessage.o \
	  RabbitConnection.o ResponseMessage.o SignInitMethod.o SignMethod.o DeleteKeyPairMethod.o \
	  GenerateRandomMethod.o SeedRandomMethod.o OpenSessionMethod.o CloseSessionMethod.o DigestInitMethod.o \
	  DigestMethod.o
	  
	
CXXFLAGS= -I../jsoncpp/ $(PRJCXXFLAGS)

-include $(OBJS:.o=.d)

all:	$(OBJS)


clean:
	-$(RM) -f $(OBJS)
