/*
 * method_message.c
 *
 *  Created on: 11-05-2013
 *      Author: franchoco
 */
#include <stdlib.h>
#include <string.h>
#include <json-c/json_object.h>
#include "method_message.h"

struct arg_list {
	char *name;
	enum arg_type type;
	union {
		int i;
		char* s;
	} val;

	struct arg_list* next;
};

struct method_message {
	char* method;
	arg_list* args;
};

// Pa que no alegue vim :P...
extern char* strdup(const char*);

arg_list*
new_arg_list(void)
{
  return NULL; /* Nadie tiene que saberlo :) */
}

static arg_list*
arg_list_i(char const* arg_name, int value, arg_list* next)
{
	arg_list* list = malloc(sizeof(*list));
	list->name = strdup(arg_name);
	list->type = INT;
	list->val.i = value;
	list->next = next;

	return list;
}

static arg_list*
arg_list_s(char const* arg_name, char const* value, arg_list* next)
{
	arg_list* list = malloc(sizeof(*list));
	list->name = strdup(arg_name);
	list->type = STRING;
	list->val.s = strdup(value);
	list->next = next;

	return list;
}

void
arg_list_add_i(arg_list** args, char const* name, int value)
{
	if (args != NULL)
		*args = arg_list_i(name, value, *args);
}

void
arg_list_add_s(arg_list** args, char const* name, char const* value)
{
	if (args != NULL)
		*args = arg_list_s(name, value, *args);
}

method_message*
new_method_message(char const* method, arg_list* args)
{
	method_message* message = malloc(sizeof(*message));
	message->method = strdup(method);
	message->args = args;

	return message;
}

void
del_arg_list(arg_list* args)
{
	if(args == NULL)
		return;
	del_arg_list(args->next);
	if(args->type == STRING) {
		free(args->val.s);
	}
	free(args->name);
	free(args->next);
}

void
del_method_message(method_message* msg)
{
	free(msg->method);
	del_arg_list(msg->args);
	free(msg->args);
	free(msg);
}


const char*
method_message_serialize(method_message const* msg)
{
	json_object* obj = json_object_new_object();

	json_object* method = json_object_new_string(msg->method);
	json_object_object_add(obj, "Method", method);

	json_object* args = json_object_new_object();
	arg_list* arg = msg->args;
	while (arg != NULL) {

		if (arg->type == INT) {
			json_object_object_add(args,
					arg->name,
					json_object_new_int(arg->val.i));

		}
		else if (arg->type == STRING) {
			json_object_object_add(args,
								arg->name,
								json_object_new_string(arg->val.s));
		}
		else {
			exit(1);
		}

		arg = arg->next;
	}

	json_object_object_add(obj, "args", args);
	return json_object_to_json_string(obj);
}

