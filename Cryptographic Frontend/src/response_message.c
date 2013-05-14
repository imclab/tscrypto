/*
 * response_message.c
 *
 *  Created on: 11-05-2013
 *      Author: franchoco
 */

#include <stdlib.h>
#include <string.h>

#include <json-c/json_object.h>
#include <json-c/json_object_iterator.h>
#include <json-c/json_tokener.h>
#include "response_message.h"


extern char* strdup(const char*);

static int 
generate_key_pair_parse(json_object* obj, response_message_t* rm)
{
  json_object* value;
  if (json_object_is_type(obj, json_type_object)) {

    if(json_object_object_get_ex(obj, "privateKeyHandler", &value)) {
      if(json_object_is_type(value, json_type_int)) 
        rm->private_key = json_object_get_int(value);
      else return -1;
    }
    else return -1;

    if(json_object_object_get_ex(obj, "publicKeyHandler", &value)) {
      if(json_object_is_type(value, json_type_int))
        rm->public_key = json_object_get_int(value);
      else return -1;
    }
    else return -1;

    return 0;
  }
  else
    return -1;
}

static int 
sign_init_parse(json_object* obj)
{
  if(json_object_is_type(obj, json_type_null))
    return 0;
  else 
    return -1;
}

static int 
sign_parse(json_object* obj, response_message_t* rm) 
{
  json_object* value;
  if (json_object_is_type(obj, json_type_object) && 
      json_object_object_get_ex(obj, "signedData", &value)) {
    if (json_object_is_type(value, json_type_string)) {
      rm->signed_data = strdup(json_object_get_string(value));
      return 0;
    }
  }

  return -1;
}

static int 
find_key_parse(json_object* obj, response_message_t* rm)
{
  json_object* value;
  if (json_object_is_type(obj, json_type_object) && 
      json_object_object_get_ex(obj, "handler", &value)) {
    if (json_object_is_type(value, json_type_int)) {
      rm->handler = json_object_get_int(value);
      return 0;
    }
  }

  return -1;
}

static int 
get_attribute_parse(json_object* obj, response_message_t* rm)
{
  json_object* value;
  if (json_object_is_type(obj, json_type_object) && 
      json_object_object_get_ex(obj, "attributeValue", &value))
  {
    if (json_object_is_type(value, json_type_string)) {
      rm->attribute = strdup(json_object_get_string(value));
      return 0;
    }
  }

  return -1;
}

response_message_t* 
new_response_message_json (char const* json, response_message_type type) 
{
  response_message_t* rm = malloc(sizeof(*rm));
  if (rm != NULL) { /* Se pudo allocar la memoria */
    rm->type = type;

    json_object* obj = json_tokener_parse(json);

    json_object* val;
    if (json_object_object_get_ex(obj, "returnCode", &val)) {
      /* ReturnCode solo puede tener strings */
      if (json_object_is_type(val, json_type_string)) {
        if (strcmp("OK", json_object_get_string(val)) != 0) 
          rm->type = ERROR;
      }
      else goto on_error;
    }

    if (json_object_object_get_ex(obj, "value", &val)) {
      /* Type-case */
      switch (rm->type) {
        case GENERATE_KEY_PAIR:
          if (generate_key_pair_parse(val, rm) == -1)
            goto on_error;
          break;
        case SIGN_INIT:
          if (sign_init_parse(val) == -1)
            goto on_error;
          break;
        case SIGN:
          if (sign_parse(val, rm) == -1)
            goto on_error;
          break;
        case FIND_KEY:
          if (find_key_parse(val, rm) == -1)
            goto on_error;
          break;
        case GET_ATTRIBUTE:
          if (get_attribute_parse(val, rm) == -1)
            goto on_error;
          break;
        case ERROR:
          if (json_object_is_type(val, json_type_string)) 
            rm->error_message = strdup(json_object_get_string(val));
          break; 
        default:
          rm->error_message = strdup("Error en la llamada.");
      }

    }

    json_object_put(obj);
  }

  return rm;

on_error:
  free(rm);
  return NULL;
}

void 
free_response_message (response_message_t* rm)
{
  if (rm != NULL) {
    if (rm->type == GENERATE_KEY_PAIR) {
    }
    else if (rm->type == SIGN_INIT) { 
    }
    else if (rm->type == SIGN) {
      free(rm->signed_data);
    }
    else if (rm->type == FIND_KEY) {
    }
    else if (rm->type == GET_ATTRIBUTE) {
      free(rm->attribute);
    }
    else if (rm->type == ERROR) {
      free(rm->error_message);
    }
  }

  free(rm);
}

