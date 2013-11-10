include Makefile.inc

MODULES = jsoncpp hsm communication base64

# Cada executable va con su propia lista de objetivos
LIB = pkcs11 # LIB agarra todo los .o de los dirs y los pone en un .so
LIB_RPATH = . # dejar vacio para ¿deploy? xD.
EXE = test
test_OBJS = test.o

PKGCONFIG_LIBS = librabbitmq botan-1.10

# Modularización...
LIB_FILENAME = $(addprefix lib,$(addsuffix .so, $(LIB)))
OBJS = $(foreach o, $(addsuffix _OBJS, $(EXE)), $($(o)))

PKGCONFIG_LDFLAGS = $(shell pkg-config --libs $(PKGCONFIG_LIBS))
PKGCONFIG_CFLAGS = $(shell pkg-config --cflags $(PKGCONFIG_LIBS))

LIB_LDFLAGS = $(PKGCONFIG_LDFLAGS) $(PRJLDFLAGS)

RPATH_OPTION = -Wl,-rpath=$(LIB_RPATH)
EXE_LDFLAGS = -L. $(addprefix -l, $(LIB)) $(if $(LIB_RPATH), $(RPATH_OPTION)) $(PKGCONFIG_LDFLAGS) $(PRJLDFLAGS)
EXE_CFLAGS = -Ihsm/cryptoki $(PKGCONFIG_CFLAGS)
EXE_CXXFLAGS = $(PKGCONFIG_CFLAGS)

CFLAGS	= $(EXE_CFLAGS) $(PRJCFLAGS) 
CXXFLAGS= $(EXE_CXXFLAGS) $(PRJCXXFLAGS) 

all:	$(EXE)
	@echo "LISTO!"

$(EXE): $(LIB_FILENAME) $(OBJS)
	$(LD) -o $@ $($@_OBJS) $(EXE_LDFLAGS)
	
$(LIB_FILENAME): $(MODULES)
	$(LD) -fpic -shared -o $@ $(addsuffix /*.o, $^) $(LIB_LDFLAGS)

$(MODULES): force_look
	@cd $@; $(MAKE) $(MFLAGS)

.PHONY:
clean:
	-$(RM) -f $(EXE) $(LIB_FILENAME) $(OBJS)
	-@for dir in $(MODULES); do (cd $$dir; $(MAKE) clean); done
	
force_look:
	@true
