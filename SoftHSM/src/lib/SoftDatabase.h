/* $Id: SoftDatabase.h 5299 2011-07-07 11:09:10Z rb $ */

/*
 * Copyright (c) 2008-2009 .SE (The Internet Infrastructure Foundation).
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/************************************************************
*
* This class handles the database.
*
************************************************************/

#ifndef SOFTHSM_SOFTDATABASE_H
#define SOFTHSM_SOFTDATABASE_H 1

#include "cryptoki.h"

// The SQLite header
#include <sqlite3.h>

// Includes for the crypto library
#include <botan/bigint.h>
#include <botan/rsa.h>

#define DB_TOKEN_LABEL 0
#define DB_TOKEN_SOPIN 1
#define DB_TOKEN_USERPIN 2

class SoftDatabase {
  public:
    SoftDatabase(char *appID);
    ~SoftDatabase();

    CK_RV init(char *dbPath);

    char* getTokenLabel();
    char* getSOPIN();
    char* getUserPIN();
    CK_RV saveTokenInfo(int valueID, char *value, int length);

    CK_BBOOL getBooleanAttribute(CK_OBJECT_HANDLE objectRef, CK_ATTRIBUTE_TYPE type, CK_BBOOL defaultValue);
    CK_OBJECT_CLASS getObjectClass(CK_OBJECT_HANDLE objectRef);
    CK_KEY_TYPE getKeyType(CK_OBJECT_HANDLE objectRef);
    Botan::BigInt getBigIntAttribute(CK_OBJECT_HANDLE objectRef, CK_ATTRIBUTE_TYPE type);
    CK_RV getAttribute(CK_OBJECT_HANDLE objectRef, CK_ATTRIBUTE *attTemplate);
    CK_RV setAttribute(CK_STATE state, CK_OBJECT_HANDLE objectRef, CK_ATTRIBUTE *attTemplate);
    CK_RV setAttributePublicKey(CK_STATE state, CK_OBJECT_HANDLE objectRef, CK_ATTRIBUTE *attTemplate);
    CK_RV setAttributePrivateKey(CK_STATE state, CK_OBJECT_HANDLE objectRef, CK_ATTRIBUTE *attTemplate);
    CK_RV setAttributeCertificate(CK_STATE state, CK_OBJECT_HANDLE objectRef, CK_ATTRIBUTE *attTemplate);
    CK_OBJECT_HANDLE* getMatchingObjects(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount, CK_ULONG *objectCount);
    CK_BBOOL hasObject(CK_OBJECT_HANDLE objectRef);
    CK_BBOOL checkAccessObj(CK_OBJECT_HANDLE objectRef);

    CK_OBJECT_HANDLE addRSAKeyPub(CK_STATE state, Botan::RSA_PrivateKey *rsaKey, CK_ATTRIBUTE_PTR pPublicKeyTemplate,
      CK_ULONG ulPublicKeyAttributeCount);
    CK_OBJECT_HANDLE addRSAKeyPriv(CK_STATE state, Botan::RSA_PrivateKey *rsaKey, CK_ATTRIBUTE_PTR pPrivateKeyTemplate,
      CK_ULONG ulPrivateKeyAttributeCount);
    CK_OBJECT_HANDLE importPublicCert(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount);
    CK_OBJECT_HANDLE importPublicKey(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount);
    CK_OBJECT_HANDLE importPrivateKey(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount);
    void destroySessObj();
    void deleteObject(CK_OBJECT_HANDLE objRef);

    CK_RV saveAttribute(CK_OBJECT_HANDLE objectID, CK_ATTRIBUTE_TYPE type, CK_VOID_PTR pValue, CK_ULONG ulValueLen);
    CK_RV saveAttributeBigInt(CK_OBJECT_HANDLE objectID, CK_ATTRIBUTE_TYPE type, Botan::BigInt *bigNumber);

  private:
    sqlite3 *db;
    char *appID;

    // Prepared statements
    sqlite3_stmt *token_info_sql;
    sqlite3_stmt *insert_token_info_sql;
    sqlite3_stmt *select_attri_id_sql;
    sqlite3_stmt *update_attribute_sql;
    sqlite3_stmt *insert_attribute_sql;
    sqlite3_stmt *insert_object_sql;
    sqlite3_stmt *select_object_id_sql;
    sqlite3_stmt *select_session_obj_sql;
    sqlite3_stmt *delete_object_sql;
    sqlite3_stmt *select_an_attribute_sql;
};

#endif /* SOFTHSM_SOFTDATABASE_H */
