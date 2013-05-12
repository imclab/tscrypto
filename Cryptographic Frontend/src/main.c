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
	arg_list* arg = new_arg_list_s("Hola", "Mundo");
	arg_list_add_i(&arg, "Chao", 1);
	arg_list_add_s(&arg, "Loco", "Loco");
	arg_list_add_s(&arg, "Perro", "Loco");
	arg_list_add_i(&arg, "Gato", 2);
	method_message* msg = new_method_message("GenerateKeyPair", arg);
	char const* json = method_message_serialize(msg);
	del_method_message(msg);
	printf("%s\n", json);

    response_message* rm = 
      new_response_message ( "{'ReturnCode' : 'OK', 'Value' : {'PrivateKeyHandler' : 123123 } }" );

    printf("%s\n", rm->value);
    del_response_message(rm);
    
	return 0;
}
