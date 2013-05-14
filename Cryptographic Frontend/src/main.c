/*
 * main.c
 *
 *  Created on: 09-05-2013
 *      Author: franchoco
 */

#include <stdio.h>

#include "method_message.h"
#include "methods.h"

int main (int argc, char **argv) {
	method_message_t* msg = generate_key_pair_method("Hola", "Mundo", "RSA", 1024);
	char const* json = method_message_serialize(msg);
	printf("%s\n", json);
	del_method_message(msg);

	return 0;
}
