#include "methods.h"
#include "method_message.h"

method_message_t*
generate_key_pair_method (char const* label, 
    char const* id, 
    char const* key_type, 
    int key_size)
{
  arg_list_t* args = new_arg_list();
  arg_list_add_s(&args, "label", label);
  arg_list_add_s(&args, "id", id);
  arg_list_add_s(&args, "keyType", key_type);
  arg_list_add_i(&args, "keySize", key_size);

  return new_method_message("GenerateKeyPair", args);
}

method_message_t*
sign_init_method (char const* mechanism, int private_key_handler)
{
  arg_list_t* args = new_arg_list();
  arg_list_add_s(&args, "mechanism", mechanism);
  arg_list_add_i(&args, "privateKeyHandler", private_key_handler);

  return new_method_message("SignInit", args);
};


method_message_t* sign_method (char const* data)
{
  arg_list_t* args = new_arg_list();
  arg_list_add_s(&args, "data", data);
  
  return new_method_message("Sign", args);
}

method_message_t* find_key_method (char const* key_type, char const* id)
{
  arg_list_t* args = new_arg_list();
  arg_list_add_s(&args, "keyType", key_type);
  arg_list_add_s(&args, "id", id);

  return new_method_message("FindKey", args);
}
