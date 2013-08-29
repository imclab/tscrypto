include ../Makefile.inc

EXE = checks
CFLAGS= -I../tcbhsm/cryptoki $(PRJCFLAGS)
CXXFLAGS= -I../tcbhsm/cryptoki $(PRJCXXFLAGS)

all:	$(EXE)

$(EXE): checks.o
	$(LD) -o $@ $^ $(EXE_LDFLAGS) -L.. -lpkcs11 -Wl,-rpath=..

clean:
	-$(RM) -f checks checks.o
