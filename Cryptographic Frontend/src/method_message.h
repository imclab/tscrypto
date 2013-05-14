/** @file method_message.h
 * Header que sirve para manejar los mensaje de envio
 * siguiendo el protocolo definido
 */

#ifndef METHOD_MESSAGE_H_
#define METHOD_MESSAGE_H_

enum arg_type { INT, STRING };

/* Declaraciones encapsuladas */
struct arg_list;
typedef struct arg_list arg_list;
struct method_message;
typedef struct method_message method_message;

/* Funciones */

/**
 * @return Una nueva lista de argumentos
 */
arg_list* new_arg_list(void);

void arg_list_add_i(arg_list** args, char const* name, int value);
void arg_list_add_s(arg_list** args, char const* name, char const* value);
method_message* new_method_message(char const* method, arg_list* args);
void del_arg_list(arg_list* args);
void del_method_message(method_message* msg);
const char* method_message_serialize(method_message const* msg);



#endif /* METHOD_MESSAGE_H_ */
