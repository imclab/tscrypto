/** @file method_message.h
 * Header que sirve para manejar los mensaje de envio
 * siguiendo el protocolo definido
 */

#ifndef METHOD_MESSAGE_H_
#define METHOD_MESSAGE_H_

enum arg_type { INT, STRING };

/* Declaraciones encapsuladas */

#ifndef ARG_LIST_T_DEFINED
#define ARG_LIST_T_DEFINED
typedef struct arg_list arg_list_t;
#endif

#ifndef METHOD_MESSAGE_T_DEFINED
#define METHOD_MESSAGE_T_DEFINED
typedef struct method_message method_message_t;
#endif
/* Funciones */
/**
 * @return Una nueva lista de argumentos
 */
arg_list_t* new_arg_list(void);

void arg_list_add_i(arg_list_t** args, char const* name, int value);
void arg_list_add_s(arg_list_t** args, char const* name, char const* value);
method_message_t* new_method_message(char const* method, arg_list_t* args);
void del_arg_list(arg_list_t* args);
void del_method_message(method_message_t* msg);
const char* method_message_serialize(method_message_t const* msg);



#endif /* METHOD_MESSAGE_H_ */
