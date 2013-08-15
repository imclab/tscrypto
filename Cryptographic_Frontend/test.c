#include <cryptoki.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void
check_return_value(CK_RV rv, const char *message)
{
        if (rv != CKR_OK) {
                fprintf(stderr, "Error at %s: %u\n",
                        message, (unsigned int)rv);
                exit(EXIT_FAILURE);
        }
}

CK_RV
initialize()
{
        return C_Initialize(NULL);
}

CK_SLOT_ID
get_slot()
{
     CK_RV rv;
     CK_SLOT_ID slotId;
     CK_ULONG slotCount = 10;
     CK_SLOT_ID *slotIds = malloc(sizeof(CK_SLOT_ID) * slotCount);

     rv = C_GetSlotList(CK_TRUE, slotIds, &slotCount);
     check_return_value(rv, "get slot list");

     if (slotCount < 1) {
          fprintf(stderr, "Error; could not find any slots\n");
          exit(1);
     }

     slotId = slotIds[0];
     free(slotIds);
     printf("slot count: %d\n", (int)slotCount);
     return slotId;
}

CK_SESSION_HANDLE
start_session(CK_SLOT_ID slotId)
{
     CK_RV rv;
     CK_SESSION_HANDLE session;
     rv = C_OpenSession(slotId,
                        CKF_SERIAL_SESSION | CKF_RW_SESSION,
                        NULL,
                        NULL,
                        &session);
     check_return_value(rv, "open session");
     return session;
}

void
login(CK_SESSION_HANDLE session, CK_BYTE *pin)
{
     CK_RV rv;
     if (pin) {
          rv = C_Login(session, CKU_USER, pin, strlen((char *)pin));
          check_return_value(rv, "log in");
     }
}

void
logout(CK_SESSION_HANDLE session)
{
     CK_RV rv;
     rv = C_Logout(session);
     if (rv != CKR_USER_NOT_LOGGED_IN) {
          check_return_value(rv, "log out");
     }
}

void
end_session(CK_SESSION_HANDLE session)
{
        CK_RV rv;
        rv = C_CloseSession(session);
        check_return_value(rv, "close session");
}

void
finalize()
{
        C_Finalize(NULL);
}

void
show_key_info(CK_SESSION_HANDLE session, CK_OBJECT_HANDLE key)
{
     CK_RV rv;
     CK_UTF8CHAR *label = (CK_UTF8CHAR *) malloc(80);
     CK_BYTE *id = (CK_BYTE *) malloc(10);
     size_t label_len;
     char *label_str;

     memset(id, 0, 10);

     CK_ATTRIBUTE template[] = {
          {CKA_LABEL, label, 80},
          {CKA_ID, id, 1}
     };

     rv = C_GetAttributeValue(session, key, template, 2);
     check_return_value(rv, "get attribute value");

     fprintf(stdout, "Found a key:\n");
     label_len = template[0].ulValueLen;
     if (label_len > 0) {
          label_str = malloc(label_len + 1);
          memcpy(label_str, label, label_len);
          label_str[label_len] = '\0';
          fprintf(stdout, "\tKey label: %s\n", label_str);
          free(label_str);
     } else {
          fprintf(stdout, "\tKey label too large, or not found\n");
     }
     if (template[1].ulValueLen > 0) {
          fprintf(stdout, "\tKey ID: %02x\n", id[0]);
     } else {
          fprintf(stdout, "\tKey id too large, or not found\n");
     }

     free(label);
     free(id);
}

void
read_private_keys(session)
{
     CK_RV rv;
     CK_OBJECT_CLASS keyClass = CKO_PRIVATE_KEY;
     CK_ATTRIBUTE template[] = {
          { CKA_CLASS, &keyClass, sizeof(keyClass) }
     };
     CK_ULONG objectCount;
     CK_OBJECT_HANDLE object;

     rv = C_FindObjectsInit(session, template, 1);
     check_return_value(rv, "Find objects init");

     rv = C_FindObjects(session, &object, 1, &objectCount);
     check_return_value(rv, "Find first object");

     while (objectCount > 0) {
          show_key_info(session, object);

          rv = C_FindObjects(session, &object, 1, &objectCount);
          check_return_value(rv, "Find other objects");
     }

     rv = C_FindObjectsFinal(session);
     check_return_value(rv, "Find objects final");
}

void
create_key_pair(CK_SESSION_HANDLE session)
{
     CK_RV rv;
     CK_OBJECT_HANDLE publicKey, privateKey;
     CK_MECHANISM mechanism = {
          CKM_RSA_PKCS_KEY_PAIR_GEN, NULL_PTR, 0
     };
     CK_ULONG modulusBits = 1024;
     CK_BYTE publicExponent[] = { 1, 0, 1 };
     CK_BYTE subject[] = "mykey";
     CK_BYTE id[] = {0xa1};
     CK_BBOOL true = CK_TRUE;
     CK_ATTRIBUTE publicKeyTemplate[] = {
          {CKA_ID, id, 3},
          {CKA_LABEL, subject, 5},
          {CKA_TOKEN, &true, sizeof(true)},
          {CKA_ENCRYPT, &true, sizeof(true)},
          {CKA_VERIFY, &true, sizeof(true)},
          {CKA_WRAP, &true, sizeof(true)},
          {CKA_MODULUS_BITS, &modulusBits, sizeof(modulusBits)},
          {CKA_PUBLIC_EXPONENT, publicExponent, 3}
     };
     CK_ATTRIBUTE privateKeyTemplate[] = {
          {CKA_ID, id, sizeof(id)},
          {CKA_LABEL, subject, 5},
          {CKA_TOKEN, &true, sizeof(true)},
          {CKA_PRIVATE, &true, sizeof(true)},
          {CKA_SENSITIVE, &true, sizeof(true)},
          {CKA_DECRYPT, &true, sizeof(true)},
          {CKA_SIGN, &true, sizeof(true)},
          {CKA_UNWRAP, &true, sizeof(true)}
     };

     rv = C_GenerateKeyPair(session,
                            &mechanism,
                            publicKeyTemplate, 8,
                            privateKeyTemplate, 8,
                            &publicKey,
                            &privateKey);
     check_return_value(rv, "generate key pair");
}

int
main(int argc, char **argv)
{
     CK_SLOT_ID slot;
     CK_SESSION_HANDLE session;
     CK_BYTE *user_pin = "1234";

     initialize();
     slot = get_slot();

     session = start_session(slot);
     if (user_pin) {
          login(session, user_pin);
     }
     
     create_key_pair(session);
     
     if (user_pin) {
          logout(session);
     }
     end_session(session);

     finalize();

     return EXIT_SUCCESS;
}

