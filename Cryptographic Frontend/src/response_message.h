/** @file response_message.h
 * Header que maneja los mensajes de respuesta
 */

#ifndef RESPONSE_MESSAGE_H_
#define RESPONSE_MESSAGE_H_
#include <stdbool.h>

typedef enum { 
  GENERATE_KEY_PAIR, 
  SIGN_INIT, 
  SIGN, 
  FIND_KEY, 
  GET_ATTRIBUTE, 
  ERROR 
} response_message_type;

/* Para hacer codigo más safe se podría esconder esta estructura
 * y acceder siempre a través de funciones
 */
typedef struct response_message {
  response_message_type type;
  union {
    struct { /* GENERATE KEY PAIR */ 
      int private_key;
      int public_key;
    };
    /* SIGN INIT (NADA XD) */
    char* signed_data; /* SIGN */
    int handler; /* FIND KEY MESSAGE */
    char* attribute; /* GET ATTRIBUTE */
    char* error_message; /* ERROR */
  };
} response_message_t;

/**
 * @param json El cstring en formato json a partir del cual 
 * hay que generar el mensaje.
 * @param type El tipo del mensaje a parsear.
 * @return Una nueva estructura generada a partir de los parametros
 */
response_message_t* new_response_message (char const* json, response_message_type type);
/**
 * @param rm El mensaje a ser liberado
 */
void response_message_free (response_message_t* rm);


#endif /* RESPONSE_MESSAGE_H_ */
