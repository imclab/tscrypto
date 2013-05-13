/*
 * response_message.h
 *
 *  Created on: 11-05-2013
 *      Author: franchoco
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

response_message_t* new_response_message (char const* json, response_message_type type);
void response_message_free (response_message_t* rm);

#endif /* RESPONSE_MESSAGE_H_ */
