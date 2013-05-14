/*
 * main.c
 *
 *  Created on: 09-05-2013
 *      Author: franchoco
 */

#include <stdio.h>

#include "method_message.h"
#include "response_message.h"

int main (int argc, char **argv) {
	arg_list* args = new_arg_list();
	arg_list_add_i(&args, "KeySize", 1024);
	arg_list_add_i(&args, "Exponent", 65537);
	method_message* msg = new_method_message("GenerateKeyPair", args);
	char const* json = method_message_serialize(msg);
	printf("%s\n", json);
	del_method_message(msg);

	return 0;
}
