/*
 * method_message.h
 *
 *  Created on: 11-05-2013
 *      Author: franchoco
 */

#ifndef METHOD_MESSAGE_H_
#define METHOD_MESSAGE_H_

enum arg_type { INT, STRING };
struct arg_list {
	char *name;
	enum arg_type type;
	union {
		int i;
		char* s;
	} val;

	struct arg_list* next;
};
typedef struct arg_list arg_list;

struct method_message {
	char* method;
	arg_list* args;
};
typedef struct method_message method_message;

/* Funciones */
arg_list* new_arg_list_i(char const* arg_name, int value);
arg_list* new_arg_list_s(char const* arg_name, char const* value);
void arg_list_add_i(arg_list** args, char const* name, int value);
void arg_list_add_s(arg_list** args, char const* name, char const* value);
method_message* new_method_message(char const* method, arg_list* args);
void del_arg_list(arg_list* args);
void del_method_message(method_message* msg);
const char* method_message_serialize(method_message const* msg);



#endif /* METHOD_MESSAGE_H_ */
