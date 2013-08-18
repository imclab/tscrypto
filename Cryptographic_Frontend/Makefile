include Makefile.inc

DIRS = cf tcbhsm

# Cada executable va con su propia lista de objetivos
# Supone que todo se compila contra las mismas librerías =)
EXE = hsm test
hsm_OBJS = main.o
test_OBJS = test.o

LIBS = tcbhsm cf # El minimo comun multiplo de todas las librerias
PKGCONFIG_LIBS = librabbitmq

# Modularización...
OBJS = $(foreach o, $(addsuffix _OBJS, $(EXE)), $($(o)))
LIBS_FILENAMES = $(addprefix lib, $(addsuffix .a, $(LIBS)))
LOCAL_LDFLAGS = -L. $(addprefix -l, $(LIBS)) $(shell pkg-config --libs $(PKGCONFIG_LIBS))
LOCAL_CFLAGS = -Itcbhsm/cryptoki $(shell pkg-config --cflags $(PKGCONFIG_LIBS))
LOCAL_CXXFLAGS = $(shell pkg-config --cflags $(PKGCONFIG_LIBS))

LDFLAGS	= $(LOCAL_LDFLAGS) $(PRJLDFLAGS)
CFLAGS	= $(LOCAL_CFLAGS) $(PRJCFLAGS) 
CXXFLAGS= $(LOCAL_CXXFLAGS) $(PRJCXXFLAGS) 

all:	$(EXE)

$(EXE): $(LIBS) $(OBJS)
	$(LD) -o $@ $($@_OBJS) $(LDFLAGS)

$(LIBS): force_look
	@cd $@; $(MAKE) $(MFLAGS)

clean:
	-$(RM) -f $(OBJS) $(LIBS_FILENAMES)
	-@for dir in $(DIRS); do (cd $$dir; $(MAKE) clean); done

force_look:
	@true
