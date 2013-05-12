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

response_message* new_response_message (char const* json) 
{
  response_message* ret = malloc(sizeof(*ret));
  ret->return_code = NULL;
  ret->value = NULL;

  struct json_object_iterator it;
  struct json_object_iterator itEnd;
  json_object* obj;

  obj = json_tokener_parse(json);
  it = json_object_iter_begin(obj);
  itEnd = json_object_iter_end(obj);

  while (!json_object_iter_equal(&it, &itEnd)) {
    char const* key = json_object_iter_peek_name (&it);
    json_object* val = json_object_iter_peek_value(&it);

    if (strcmp("ReturnCode", key) == 0) { 
      /* ReturnCode solo puede tener strings */
      if (json_object_is_type(val, json_type_string))
        ret->return_code = strdup(json_object_get_string(val));
      else 
        goto exit_with_errors;

    } else if (strcmp("Value", key) == 0) {
      if(json_object_is_type(val, json_type_null))
        ret->return_code = NULL;
      else 
        ret->value = strdup(json_object_get_string(val));
    }

    json_object_iter_next(&it);
  }
  
  if (ret->return_code == NULL)
    goto exit_with_errors;

  return ret;

exit_with_errors:
  free(ret);
  return NULL;
  
}

void del_response_message (response_message* rm)
{
  if (rm != NULL) {
    free(rm->return_code);
    free(rm->value);
  }

  free(rm);
}


