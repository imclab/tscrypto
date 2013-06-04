/* $Id: SoftHSMInternal.cpp 6177 2012-02-21 06:53:10Z rb $ */

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
* This class handles the internal state.
* Mainly session and object handling.
*
************************************************************/

#include <config.h>
#include "SoftHSMInternal.h"
#include "log.h"
#include "attribute.h"
#include "userhandling.h"
#include "tokenhandling.h"
#include "util.h"

// Standard includes
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>
#include <time.h>

// Includes for the crypto library
#include <botan/pipe.h>
#include <botan/filters.h>
#include <botan/hex.h>
#include <botan/sha2_32.h>

SoftHSMInternal::SoftHSMInternal() {
  openSessions = 0;

  for(int i = 0; i < MAX_SESSION_COUNT; i++) {
    sessions[i] = NULL_PTR;
  }
  sessionsMutex = MutexFactory::i()->getMutex();

  slots = new SoftSlot();

  time_t rawtime;
  time(&rawtime);
  char dateTime[15];
  strftime(dateTime, 15, "%Y%m%d%H%M%S", gmtime(&rawtime));

  snprintf(appID, 32, "%s-%010i", dateTime, getpid());
}

SoftHSMInternal::~SoftHSMInternal() {
  for(int i = 0; i < MAX_SESSION_COUNT; i++) {
    if(sessions[i] != NULL_PTR) {
      // Remove the session objects created by this session
      sessions[i]->db->destroySessObj();
      delete sessions[i];
      sessions[i] = NULL_PTR;
    }
  }

  openSessions = 0;

  DELETE_PTR(slots);

  MutexFactory::i()->recycleMutex(sessionsMutex);
  MutexFactory::destroy();
}

int SoftHSMInternal::getSessionCount() {
  return openSessions;
}

// Creates a new session if there is enough space available.

CK_RV SoftHSMInternal::openSession(CK_SLOT_ID slotID, CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY Notify, CK_SESSION_HANDLE_PTR phSession) {
  SoftSlot *currentSlot = slots->getSlot(slotID);

  MutexLocker lock(sessionsMutex);

  CHECK_DEBUG_RETURN(currentSlot == NULL_PTR, "C_OpenSession", "The given slotID does not exist",
                     CKR_SLOT_ID_INVALID);
  CHECK_DEBUG_RETURN((currentSlot->slotFlags & CKF_TOKEN_PRESENT) == 0, "C_OpenSession", "The token is not present",
                     CKR_TOKEN_NOT_PRESENT);
  CHECK_DEBUG_RETURN((currentSlot->tokenFlags & CKF_TOKEN_INITIALIZED) == 0, "C_OpenSession", "The token is not initialized",
                     CKR_TOKEN_NOT_RECOGNIZED);
  CHECK_DEBUG_RETURN(openSessions >= MAX_SESSION_COUNT, "C_OpenSession", "Can not open more sessions. Have reached the maximum number.",
                     CKR_SESSION_COUNT);
  CHECK_DEBUG_RETURN((flags & CKF_SERIAL_SESSION) == 0, "C_OpenSession", "Can not open a non parallel session",
                     CKR_SESSION_PARALLEL_NOT_SUPPORTED);
  CHECK_DEBUG_RETURN((flags & CKF_RW_SESSION) == 0 && currentSlot->soPIN != NULL_PTR, "C_OpenSession", 
                     "Can not open a Read-Only session when in SO mode", CKR_SESSION_READ_WRITE_SO_EXISTS);
  CHECK_DEBUG_RETURN(!phSession, "C_OpenSession", "phSession must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);

  for(int i = 0; i < MAX_SESSION_COUNT; i++) {
    if(sessions[i] == NULL_PTR) {
      sessions[i] = new SoftSession(flags & CKF_RW_SESSION, currentSlot, appID);

      // Check that we have a connection
      if(sessions[i]->db == NULL_PTR) {
        delete sessions[i];
        sessions[i] = NULL_PTR;
        DEBUG_MSG("C_OpenSession", "Could not connect to database.");
        return CKR_GENERAL_ERROR;
      }

      sessions[i]->pApplication = pApplication;
      sessions[i]->Notify = Notify;
      *phSession = (CK_SESSION_HANDLE)(i+1);
      openSessions++;

      DEBUG_MSG("C_OpenSession", "OK");
      return CKR_OK;
    }
  }

  DEBUG_MSG("C_OpenSession", "Can not open more sessions. Have reached the maximum number.");
  return CKR_SESSION_COUNT;
}

// Closes the specific session.

CK_RV SoftHSMInternal::closeSession(CK_SESSION_HANDLE hSession) {
  int sessID = hSession - 1;

  MutexLocker lock(sessionsMutex);

  CHECK_DEBUG_RETURN(hSession > MAX_SESSION_COUNT || hSession < 1 || sessions[sessID] == NULL_PTR, "C_CloseSession", 
                     "The session does not exist", CKR_SESSION_HANDLE_INVALID);

  SoftSession *curSession = sessions[sessID];

  // Check if this is the last session on the token
  CK_BBOOL lastSessOnT = CK_TRUE;
  CK_SLOT_ID slotID = curSession->currentSlot->getSlotID();
  for (int i = 0; i < MAX_SESSION_COUNT; i++) {
    if(sessions[i] != NULL_PTR && sessID != i) {
      if(sessions[i]->currentSlot->getSlotID() == slotID) {
        lastSessOnT = CK_FALSE;
        break;
      }
    }
  }

  // Last session for this token? Log out.
  if(lastSessOnT == CK_TRUE) {
    FREE_PTR(curSession->currentSlot->userPIN);
    FREE_PTR(curSession->currentSlot->soPIN);
  }

  // Remove the session objects created by this session
  sessions[sessID]->db->destroySessObj();

  // Close the current session;
  delete sessions[sessID];
  sessions[sessID] = NULL_PTR;
  openSessions--;

  DEBUG_MSG("C_CloseSession", "OK");
  return CKR_OK;
}

// Closes all the sessions.

CK_RV SoftHSMInternal::closeAllSessions(CK_SLOT_ID slotID) {
  SoftSlot *currentSlot = slots->getSlot(slotID);

  MutexLocker lock(sessionsMutex);

  CHECK_DEBUG_RETURN(currentSlot == NULL_PTR, "C_CloseAllSessions", "The given slotID does not exist",
                     CKR_SLOT_ID_INVALID);

  // Close all sessions on the slot.
  for (int i = 0; i < MAX_SESSION_COUNT; i++) {
    if(sessions[i] != NULL_PTR) {
      if(sessions[i]->currentSlot->getSlotID() == slotID) {
        // Remove session objects
        sessions[i]->db->destroySessObj();

        // Close session
        delete sessions[i];
        sessions[i] = NULL_PTR;
        openSessions--;
      }
    }
  }

  // Log out from the slot
  FREE_PTR(currentSlot->userPIN);
  FREE_PTR(currentSlot->soPIN);

  DEBUG_MSG("C_CloseAllSessions", "OK");
  return CKR_OK;
}

// Return information about the session.

CK_RV SoftHSMInternal::getSessionInfo(CK_SESSION_HANDLE hSession, CK_SESSION_INFO_PTR pInfo) {
  SoftSession *session = getSession(hSession);

  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_GetSessionInfo", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);
  CHECK_DEBUG_RETURN(pInfo == NULL_PTR, "C_GetSessionInfo", "pInfo must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);

  pInfo->slotID = session->currentSlot->getSlotID();
  pInfo->state = session->getSessionState();
  pInfo->flags = CKF_SERIAL_SESSION;
  if(session->isReadWrite()) {
    pInfo->flags |= CKF_RW_SESSION;
  }
  pInfo->ulDeviceError = 0;

  DEBUG_MSG("C_GetSessionInfo", "OK");
  return CKR_OK;
}

// Logs the user into the token.

CK_RV SoftHSMInternal::login(CK_SESSION_HANDLE hSession, CK_USER_TYPE userType, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) {
  SoftSession *session = getSession(hSession);

  MutexLocker lock(sessionsMutex);

  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_Login", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);
  CHECK_DEBUG_RETURN(pPin == NULL_PTR, "C_Login", "pPin must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);

  if(ulPinLen < MIN_PIN_LEN || ulPinLen > MAX_PIN_LEN) {
    if(userType == CKU_SO) {
      session->currentSlot->tokenFlags |= CKF_SO_PIN_COUNT_LOW;
    } else {
      session->currentSlot->tokenFlags |= CKF_USER_PIN_COUNT_LOW;
    }
    DEBUG_MSG("C_Login", "Incorrent PIN length");
    return CKR_PIN_INCORRECT;
  }

  int logInType = CKU_USER;

  CK_SLOT_ID slotID = session->currentSlot->getSlotID();
  switch(userType) {
    case CKU_SO:
      // Only one user type can be logged in
      CHECK_DEBUG_RETURN(session->currentSlot->userPIN != NULL_PTR, "C_Login", "A normal user is already logged in",
                         CKR_USER_ANOTHER_ALREADY_LOGGED_IN);

      // Check that we have no R/O session with the slot
      for (int i = 0; i < MAX_SESSION_COUNT; i++) {
        if(sessions[i] != NULL_PTR) {
          CHECK_DEBUG_RETURN(sessions[i]->currentSlot->getSlotID() == slotID && sessions[i]->isReadWrite() == CK_FALSE, 
                             "C_Login", "No read only session must exist", CKR_SESSION_READ_ONLY_EXISTS);
        }
      }
      logInType = CKU_SO;
      break;
    case CKU_USER:
      // Only one user type can be logged in
      CHECK_DEBUG_RETURN(session->currentSlot->soPIN != NULL_PTR, "C_Login", "A SO is already logged in",
                         CKR_USER_ANOTHER_ALREADY_LOGGED_IN);

      CHECK_DEBUG_RETURN(session->currentSlot->hashedUserPIN == NULL_PTR, "C_Login", "The normal user PIN is not initialized",
                         CKR_USER_PIN_NOT_INITIALIZED);
      break;
    case CKU_CONTEXT_SPECIFIC:
      CHECK_DEBUG_RETURN(session->currentSlot->userPIN == NULL_PTR && session->currentSlot->soPIN == NULL_PTR, "C_Login", 
                         "A previous login must have been performed", CKR_OPERATION_NOT_INITIALIZED);

      if(session->currentSlot->soPIN != NULL_PTR) {
        logInType = CKU_SO;
      }
      break;
    default:
      DEBUG_MSG("C_Login", "The given user type does not exist");
      return CKR_USER_TYPE_INVALID;
      break;
  }

  // Digest the PIN
  char *tmpPIN = digestPIN(pPin, ulPinLen);

  if(logInType == CKU_SO) {
    // Is the PIN incorrect?
    if(strcmp(tmpPIN, session->currentSlot->hashedSOPIN) != 0) {
      free(tmpPIN);

      session->currentSlot->tokenFlags |= CKF_SO_PIN_COUNT_LOW;

      DEBUG_MSG("C_Login", "The SO PIN is incorrect");
      return CKR_PIN_INCORRECT;
    }

    free(tmpPIN);

    // First login?
    if(session->currentSlot->soPIN == NULL_PTR) {
      // Store the PIN
      session->currentSlot->soPIN = (char *)malloc(ulPinLen + 1);
      CHECK_DEBUG_RETURN(session->currentSlot->soPIN == NULL_PTR, "C_Login", "Could not allocate memory", CKR_HOST_MEMORY);
      session->currentSlot->soPIN[ulPinLen] = '\0';
      memcpy(session->currentSlot->soPIN, pPin, ulPinLen);
    }

    session->currentSlot->tokenFlags &= ~CKF_SO_PIN_COUNT_LOW;

    DEBUG_MSG("C_Login", "OK");
    return CKR_OK;
  } else {
    // Is the PIN incorrect?
    if(strcmp(tmpPIN, session->currentSlot->hashedUserPIN) != 0) {
      free(tmpPIN);

      session->currentSlot->tokenFlags |= CKF_USER_PIN_COUNT_LOW;

      DEBUG_MSG("C_Login", "The user PIN is incorrect");
      return CKR_PIN_INCORRECT;
    }

    free(tmpPIN);

    // First login?
    if(session->currentSlot->userPIN == NULL_PTR) {
      // Store the PIN
      session->currentSlot->userPIN = (char *)malloc(ulPinLen + 1);
      CHECK_DEBUG_RETURN(session->currentSlot->userPIN == NULL_PTR, "C_Login", "Could not allocate memory", CKR_HOST_MEMORY);
      session->currentSlot->userPIN[ulPinLen] = '\0';
      memcpy(session->currentSlot->userPIN, pPin, ulPinLen);
    }

    session->currentSlot->tokenFlags &= ~CKF_USER_PIN_COUNT_LOW;

    DEBUG_MSG("C_Login", "OK");
    return CKR_OK;
  }
}

// Logs out the user from the token.

CK_RV SoftHSMInternal::logout(CK_SESSION_HANDLE hSession) {
  SoftSession *session = getSession(hSession);

  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_Logout", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);

  FREE_PTR(session->currentSlot->userPIN);
  FREE_PTR(session->currentSlot->soPIN);

  DEBUG_MSG("C_Logout", "OK");
  return CKR_OK;
}

// Retrieves the session pointer associated with the session handle.

SoftSession* SoftHSMInternal::getSession(CK_SESSION_HANDLE hSession) {
  if(hSession > MAX_SESSION_COUNT || hSession < 1) {
    return NULL_PTR;
  }

  return sessions[hSession-1];
}

// Init the token

CK_RV SoftHSMInternal::initToken(CK_SLOT_ID slotID, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen, 
      CK_UTF8CHAR_PTR pLabel) {

  CHECK_DEBUG_RETURN(pPin == NULL_PTR, "C_InitToken", "pPin must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);
  CHECK_DEBUG_RETURN(pLabel == NULL_PTR, "C_InitToken", "pLabel must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);

  SoftSlot *currentSlot = slots->getSlot(slotID);

  CHECK_DEBUG_RETURN(currentSlot == NULL_PTR, "C_InitToken", "The given slotID does not exist",
                     CKR_SLOT_ID_INVALID);
  CHECK_DEBUG_RETURN((currentSlot->slotFlags & CKF_TOKEN_PRESENT) == 0, "C_InitToken",
                     "The token is not present", CKR_TOKEN_NOT_PRESENT);

  // Check that we have no session with the slot.
  MutexLocker lock(sessionsMutex);
  for (int i = 0; i < MAX_SESSION_COUNT; i++) {
    if(sessions[i] != NULL_PTR) {
      CHECK_DEBUG_RETURN(sessions[i]->currentSlot->getSlotID() == slotID, "C_InitToken",
                         "A session is open with the slot", CKR_SESSION_EXISTS);
    }
  }

  CHECK_DEBUG_RETURN(ulPinLen < MIN_PIN_LEN || ulPinLen > MAX_PIN_LEN, "C_InitToken", "Incorrent PIN length",
                     CKR_PIN_INCORRECT);

  return softInitToken(currentSlot, pPin, ulPinLen, pLabel);
}

// Init the user PIN

CK_RV SoftHSMInternal::initPIN(CK_SESSION_HANDLE hSession, CK_UTF8CHAR_PTR pPin, CK_ULONG ulPinLen) {
  SoftSession *session = getSession(hSession);
  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_InitPIN", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);

  CHECK_DEBUG_RETURN(pPin == NULL_PTR, "C_InitPIN", "pTemplate must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);
  CHECK_DEBUG_RETURN(session->getSessionState() != CKS_RW_SO_FUNCTIONS, "C_InitPIN", "Must be in R/W SO Functions state",
                     CKR_USER_NOT_LOGGED_IN);
  CHECK_DEBUG_RETURN(ulPinLen < MIN_PIN_LEN || ulPinLen > MAX_PIN_LEN, "C_InitPIN", "Incorrent PIN length",
                     CKR_PIN_LEN_RANGE);

  // Save the User PIN in db
  char *hashedPIN = digestPIN(pPin, ulPinLen);
  CHECK_DEBUG_RETURN(hashedPIN == NULL_PTR, "C_InitPIN", "Could not allocate memory",
                     CKR_HOST_MEMORY);
  session->db->saveTokenInfo(DB_TOKEN_USERPIN, hashedPIN, strlen(hashedPIN));
  session->currentSlot->readDB();
  free(hashedPIN);

  DEBUG_MSG("C_InitPIN", "OK");
  return CKR_OK;
}

// Change the pin of current user

CK_RV SoftHSMInternal::setPIN(CK_SESSION_HANDLE hSession, CK_UTF8CHAR_PTR pOldPin, CK_ULONG ulOldLen, 
      CK_UTF8CHAR_PTR pNewPin, CK_ULONG ulNewLen) {

  SoftSession *session = getSession(hSession);
  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_SetPIN", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);

  CHECK_DEBUG_RETURN(pOldPin == NULL_PTR, "C_SetPIN", "pOldPin must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);
  CHECK_DEBUG_RETURN(pNewPin == NULL_PTR, "C_SetPIN", "pNewPin must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);
  CHECK_DEBUG_RETURN(ulOldLen < MIN_PIN_LEN || ulOldLen > MAX_PIN_LEN, "C_SetPIN", "Incorrent PIN length",
                     CKR_PIN_LEN_RANGE);
  CHECK_DEBUG_RETURN(ulNewLen < MIN_PIN_LEN || ulNewLen > MAX_PIN_LEN, "C_SetPIN", "Incorrent PIN length",
                     CKR_PIN_LEN_RANGE);

  // Digest the given old PIN
  char *hashedOldPIN = digestPIN(pOldPin, ulOldLen);
  CHECK_DEBUG_RETURN(hashedOldPIN == NULL_PTR, "C_SetPIN", "Could not allocate memory",
                     CKR_HOST_MEMORY);

  // Create a copy of the new PIN
  char *newPIN = (char *)malloc(ulNewLen + 1);
  if(newPIN == NULL_PTR) {
    free(hashedOldPIN);
    DEBUG_MSG("C_SetPIN", "Could not allocate memory");
    return CKR_HOST_MEMORY;
  } 
  newPIN[ulNewLen] = '\0';
  memcpy(newPIN, pNewPin, ulNewLen);

  int pinType = DB_TOKEN_USERPIN;

  switch(session->getSessionState()) {
    case CKS_RW_SO_FUNCTIONS:
      pinType = DB_TOKEN_SOPIN;

      // Check that the PIN matches
      if(strcmp(hashedOldPIN, session->currentSlot->hashedSOPIN) != 0) {
        free(hashedOldPIN);
        free(newPIN);

        DEBUG_MSG("C_SetPIN", "The given SO PIN is incorrect");
        return CKR_PIN_INCORRECT;
      }

      // Switch to the new PIN for this user
      FREE_PTR(session->currentSlot->soPIN);
      session->currentSlot->soPIN = newPIN;
      break;
    case CKS_RW_USER_FUNCTIONS:
      // Check that the PIN matches
      if(strcmp(hashedOldPIN, session->currentSlot->hashedUserPIN) != 0) {
        free(hashedOldPIN);
        free(newPIN);

        DEBUG_MSG("C_SetPIN", "The given User PIN is incorrect");
        return CKR_PIN_INCORRECT;
      }

      // Switch to the new PIN for this user
      FREE_PTR(session->currentSlot->userPIN);
      session->currentSlot->userPIN = newPIN;
      break;
    case CKS_RW_PUBLIC_SESSION:
      free(newPIN);

      // We are not logged in, so we have to check if the PIN is initialized
      if(session->currentSlot->hashedUserPIN == NULL_PTR) {
        free(hashedOldPIN);

        DEBUG_MSG("C_SetPIN", "The User PIN is not initialized");
        return CKR_PIN_INCORRECT;
      }
      // Check that the PIN matches
      if(strcmp(hashedOldPIN, session->currentSlot->hashedUserPIN) != 0) {
        free(hashedOldPIN);

        DEBUG_MSG("C_SetPIN", "The given User PIN is incorrect");
        return CKR_PIN_INCORRECT;
      }
      break;
    default:
      free(hashedOldPIN);
      free(newPIN);
      DEBUG_MSG("C_SetPIN", "Must be in a RW session");
      return CKR_SESSION_READ_ONLY;
  }

  free(hashedOldPIN);

  // Save new PIN in db
  char *hashedNewPIN = digestPIN(pNewPin, ulNewLen);
  session->db->saveTokenInfo(pinType, hashedNewPIN, strlen(hashedNewPIN));
  session->currentSlot->readDB();

  DEBUG_MSG("C_SetPIN", "OK");
  return CKR_OK;
}

// Retrieves the attributes specified by the template.
// There can be different error states depending on 
// if the given buffer is too small, the attribute is 
// sensitive, or not supported by the object.
// If there is an error, then the most recent one is
// returned.

CK_RV SoftHSMInternal::getAttributeValue(CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE hObject, 
    CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) {

  SoftSession *session = getSession(hSession);
  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_GetAttributeValue", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);

  CK_BBOOL hasObject = session->db->hasObject(hObject);
  CHECK_DEBUG_RETURN(hasObject == CK_FALSE, "C_GetAttributeValue", "Can not find the object",
                     CKR_OBJECT_HANDLE_INVALID);

  CK_BBOOL userAuth = userAuthorization(session->getSessionState(), session->db->getBooleanAttribute(hObject, CKA_TOKEN, CK_TRUE),
                                        session->db->getBooleanAttribute(hObject, CKA_PRIVATE, CK_TRUE), 0);
  CHECK_DEBUG_RETURN(userAuth == CK_FALSE, "C_GetAttributeValue", "User is not authorized",
                     CKR_OBJECT_HANDLE_INVALID);

  CHECK_DEBUG_RETURN(pTemplate == NULL_PTR, "C_GetAttributeValue", "pTemplate must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);

  CK_RV result = CKR_OK;
  CK_RV objectResult = CKR_OK;

  for(CK_ULONG i = 0; i < ulCount; i++) {
    objectResult = session->db->getAttribute(hObject, &pTemplate[i]);
    if(objectResult != CKR_OK) {
      result = objectResult;
    }
  }

  DEBUG_MSG("C_GetAttributeValue", "Returning");
  return result;
}

// Set the attributes according to the template.

CK_RV SoftHSMInternal::setAttributeValue(CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE hObject, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) {
  SoftSession *session = getSession(hSession);
  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_SetAttributeValue", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);

  CK_BBOOL hasObject = session->db->hasObject(hObject);
  CHECK_DEBUG_RETURN(hasObject == CK_FALSE, "C_SetAttributeValue", "Can not find the object",
                     CKR_OBJECT_HANDLE_INVALID);

  CK_BBOOL userAuth = userAuthorization(session->getSessionState(), session->db->getBooleanAttribute(hObject, CKA_TOKEN, CK_TRUE),
                                        session->db->getBooleanAttribute(hObject, CKA_PRIVATE, CK_TRUE), 1);
  CHECK_DEBUG_RETURN(userAuth == CK_FALSE, "C_SetAttributeValue", "User is not authorized",
                     CKR_OBJECT_HANDLE_INVALID);

  CHECK_DEBUG_RETURN(pTemplate == NULL_PTR, "C_SetAttributeValue", "pTemplate must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);

  CK_RV result = CKR_OK;
  CK_RV objectResult = CKR_OK;

  // Loop through all the attributes in the template
  for(CK_ULONG i = 0; i < ulCount; i++) {
    objectResult = session->db->setAttribute(session->getSessionState(), hObject, &pTemplate[i]);
    if(objectResult != CKR_OK) {
      result = objectResult;
    }
  }

  DEBUG_MSG("C_SetAttributeValue", "Returning");
  return result;
}

// Initialize the search for objects.
// The template specifies the search pattern.

CK_RV SoftHSMInternal::findObjectsInit(CK_SESSION_HANDLE hSession, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount) {
  SoftSession *session = getSession(hSession);
  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_FindObjectsInit", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);

  CHECK_DEBUG_RETURN(session->findInitialized, "C_FindObjectsInit", "Find is already initialized",
                     CKR_OPERATION_ACTIVE);
  CHECK_DEBUG_RETURN(pTemplate == NULL_PTR && ulCount > 0, "C_FindObjectsInit", "pTemplate must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);

  DELETE_PTR(session->findAnchor);

  // Creates the search result chain.
  session->findAnchor = new SoftFind();
  session->findCurrent = session->findAnchor;

  // Get all the matching objects
  CK_ULONG objectCount = 0;
  CK_OBJECT_HANDLE *objectRefs = session->db->getMatchingObjects(pTemplate, ulCount, &objectCount);

  // Check object access and then add the result.
  for(CK_ULONG counter = 0; counter < objectCount; counter++) {
    CK_OBJECT_HANDLE currentObject = objectRefs[counter];

    // Check user auth for object access
    CK_BBOOL userAuth = userAuthorization(session->getSessionState(), session->db->getBooleanAttribute(currentObject, CKA_TOKEN, CK_TRUE), 
                                            session->db->getBooleanAttribute(currentObject, CKA_PRIVATE, CK_TRUE), 0);
    if(userAuth == CK_TRUE) {
      session->findAnchor->addFind(currentObject);
    }
  }

  FREE_PTR(objectRefs);

  session->findInitialized = true;

  DEBUG_MSG("C_FindObjectsInit", "OK");
  return CKR_OK;
}

// Create an object

CK_RV SoftHSMInternal::createObject(CK_SESSION_HANDLE hSession, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount, CK_OBJECT_HANDLE_PTR phObject) {
  SoftSession *session = getSession(hSession);
  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_CreateObject", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);
  CHECK_DEBUG_RETURN(pTemplate == NULL_PTR, "C_CreateObject", "pTemplate must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);
  CHECK_DEBUG_RETURN(phObject == NULL_PTR, "C_CreateObject", "phObject must not be a NULL_PTR",
                     CKR_ARGUMENTS_BAD);

  CK_BBOOL isToken = CK_FALSE;
  CK_BBOOL isPrivate = CK_TRUE;
  CK_OBJECT_CLASS oClass = CKO_VENDOR_DEFINED;
  CK_KEY_TYPE keyType = CKK_VENDOR_DEFINED;

  // Extract object information
  for(CK_ULONG i = 0; i < ulCount; i++) {
    switch(pTemplate[i].type) {
      case CKA_TOKEN:
        if(pTemplate[i].ulValueLen == sizeof(CK_BBOOL)) {
          isToken = *(CK_BBOOL*)pTemplate[i].pValue;
        }
        break;
      case CKA_PRIVATE:
        if(pTemplate[i].ulValueLen == sizeof(CK_BBOOL)) {
          isPrivate = *(CK_BBOOL*)pTemplate[i].pValue;
        }
        break;
      case CKA_CLASS:
        if(pTemplate[i].ulValueLen == sizeof(CK_OBJECT_CLASS)) {
          oClass = *(CK_OBJECT_CLASS*)pTemplate[i].pValue;
        }
        break;
      case CKA_KEY_TYPE:
        if(pTemplate[i].ulValueLen == sizeof(CK_KEY_TYPE)) {
          keyType = *(CK_KEY_TYPE*)pTemplate[i].pValue;
        }
        break;
      default:
        break;
    }
  }

  CHECK_DEBUG_RETURN(isToken == CK_TRUE && session->isReadWrite() == CK_FALSE, "C_CreateObject",
                     "Only session objects can be created during a read-only session.", CKR_SESSION_READ_ONLY);
  CK_BBOOL userAuth = userAuthorization(session->getSessionState(), isToken, isPrivate, 1);
  CHECK_DEBUG_RETURN(userAuth == CK_FALSE, "C_CreateObject", "User is not authorized", CKR_USER_NOT_LOGGED_IN);

  CK_RV rv;
  CK_OBJECT_HANDLE oHandle;

  switch(oClass) {
    case CKO_CERTIFICATE:
        rv = valAttributeCertificate(session->getSessionState(), pTemplate, ulCount);
        CHECK_DEBUG_RETURN(rv != CKR_OK, "C_CreateObject", "Problem with object template", rv);
        oHandle = session->db->importPublicCert(pTemplate, ulCount);
    	break;
    case CKO_PUBLIC_KEY:
      if(keyType == CKK_RSA) {
        rv = valAttributePubRSA(session->getSessionState(), pTemplate, ulCount);
        CHECK_DEBUG_RETURN(rv != CKR_OK, "C_CreateObject", "Problem with object template", rv);
        oHandle = session->db->importPublicKey(pTemplate, ulCount);
      } else {
        DEBUG_MSG("C_CreateObject", "The key type is not supported");
        return CKR_ATTRIBUTE_VALUE_INVALID;
      }
      break;
    case CKO_PRIVATE_KEY:
      if(keyType == CKK_RSA) {
        rv = valAttributePrivRSA(session->getSessionState(), session->rng, pTemplate, ulCount);
        CHECK_DEBUG_RETURN(rv != CKR_OK, "C_CreateObject", "Problem with object template", rv);
        oHandle = session->db->importPrivateKey(pTemplate, ulCount);
      } else {
        DEBUG_MSG("C_CreateObject", "The key type is not supported");
        return CKR_ATTRIBUTE_VALUE_INVALID;
      }
      break;
    default:
      DEBUG_MSG("C_CreateObject", "The object class is not supported");
      return CKR_ATTRIBUTE_VALUE_INVALID;
      break;
  }

  CHECK_DEBUG_RETURN(oHandle == CK_INVALID_HANDLE, "C_CreateObject", "Could not save info in database", CKR_GENERAL_ERROR);

  *phObject = oHandle;

  DEBUG_MSG("C_CreateObject", "OK");
  return CKR_OK;
}

// Destroys the object.

CK_RV SoftHSMInternal::destroyObject(CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE hObject) {
  SoftSession *session = getSession(hSession);
  CHECK_DEBUG_RETURN(session == NULL_PTR, "C_DestroyObject", "Can not find the session",
                     CKR_SESSION_HANDLE_INVALID);

  CK_BBOOL hasObject = session->db->hasObject(hObject);
  CHECK_DEBUG_RETURN(hasObject == CK_FALSE, "C_DestroyObject", "Can not find the object",
                     CKR_OBJECT_HANDLE_INVALID);

  CK_BBOOL userAuth = userAuthorization(session->getSessionState(), session->db->getBooleanAttribute(hObject, CKA_TOKEN, CK_TRUE),
                                        session->db->getBooleanAttribute(hObject, CKA_PRIVATE, CK_TRUE), 1);
  CHECK_DEBUG_RETURN(userAuth == CK_FALSE, "C_DestroyObject", "User is not authorized",
                     CKR_OBJECT_HANDLE_INVALID);

  // Remove the key from the sessions' key cache
  MutexLocker lock(sessionsMutex);
  for(int i = 0; i < MAX_SESSION_COUNT; i++) {
    if(sessions[i] != NULL_PTR) {
      sessions[i]->keyStore->removeKey(hObject);

      // Also reset the single element cache, so that the old pipe will not be reused
      // if the handle is reused by a new object.
      if(sessions[i]->signKey == hObject) {
        sessions[i]->signKey = CK_INVALID_HANDLE;
      }
    }
  }

  // Delete the object from the database
  session->db->deleteObject(hObject);

  INFO_MSG("C_DestroyObject", "An object has been destroyed");
  DEBUG_MSG("C_DestroyObject", "Returning");
  return CKR_OK;
}

