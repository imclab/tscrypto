/*
 * response_message.h
 *
 *  Created on: 11-05-2013
 *      Author: franchoco
 */

#ifndef RESPONSE_MESSAGE_H_
#define RESPONSE_MESSAGE_H_


struct response_message {
	char* return_code;
	char* value;
};
typedef struct response_message response_message;

response_message* new_response_message (char const* json);
void del_response_message (response_message* rm);


#endif /* RESPONSE_MESSAGE_H_ */
