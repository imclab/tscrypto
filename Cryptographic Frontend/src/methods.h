/**@file 
 * @author Francisco Cifuentes
 * */
#ifndef METHODS_H_
#define METHODS_H_

struct method_message;
typedef struct method_message method_message_t;

/**
 * @param label Etiqueda para asociar en el servidor al par de llaves.
 * @param id Identificador para asociar en el servidor al par de llaves.
 * @param key_type El tipo de llave a generar. (Javax.security)
 * @param key_size El modulo de la llave a generar.
 * @return Un nuevo mensaje a ser manipulado posteriormente.
 */
method_message_t* generate_key_pair_method (char const* label, char const* id, char const* key_type, int key_size);

/**
 * @param mechanism El algoritmo con el cual se van a firmar los datos.
 * @param private_key_handler El handler de la llave privada a ser usada para firmar.
 * @return Un nuevo mensaje a ser manipulado posteriormente.
 */
method_message_t* sign_init_method (char const* mechanism, int private_key_handler);

/**
 * @param data Los datos a ser firmados, en formato hexadecimal.
 * @return Un nuevo mensaje a ser manipulado posteriormente.
 */
method_message_t* sign_method (char const* data);

/**
 * @param key_type El tipo de llave a obtener su handler ("public" o "private")
 * @param id El identificador asignado al par
 * @return Un nuevo mensaje a ser manipulado posteriormente.
 */
method_message_t* find_key_method (char const* key_type, char const* id);

#endif
