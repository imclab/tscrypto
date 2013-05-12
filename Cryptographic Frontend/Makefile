CC=clang
LD=clang

EXE = cfrontend
OBJS = main.o method_message.o response_message.o
INCLUDE_PATH =-I./lib/json-c/include -I./lib/rabbitmq/include
LIBS =-ljson-c -lrabbitmq
LIB_PATH =-L./lib/json-c/lib -L./lib/rabbitmq/lib
SRC_PATH =./src

CCFLAGS=-g -Wall $(INCLUDE_PATH)
LDFLAGS =-static $(LIB_PATH) $(LIBS)


all: $(EXE)

$(EXE): $(OBJS)
	$(LD) -o $(EXE) $(OBJS) $(LDFLAGS) 

main.o: $(SRC_PATH)/main.c
	$(CC) -c $(SRC_PATH)/main.c $(CCFLAGS)

method_message.o: $(SRC_PATH)/method_message.c
	$(CC) -c $(SRC_PATH)/method_message.c $(CCFLAGS)

response_message.o: $(SRC_PATH)/response_message.c
	$(CC) -c $(SRC_PATH)/response_message.c $(CCFLAGS)

clean:
	rm *.o 

clean-all:
	rm *.o $(EXE)
