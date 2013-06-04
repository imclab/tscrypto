/* $Id: SoftKeyStore.h 5299 2011-07-07 11:09:10Z rb $ */

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
* This class handles the key cache for each session
*
* A new key is added by prepending a new KeyStore object
* and making it first in the chain.
*
* A more recent cached key is more likely to be used,
* thus is it better to have it first in the chain.
*
************************************************************/

#ifndef SOFTHSM_SOFTKEYSTORE_H
#define SOFTHSM_SOFTKEYSTORE_H 1

#include "cryptoki.h"

// Includes for the crypto library
#include <botan/pk_keys.h>

class SoftKeyStore {
  public:
    SoftKeyStore();
    ~SoftKeyStore();

    void removeKey(CK_OBJECT_HANDLE removeIndex);
    Botan::Public_Key *getKey(CK_OBJECT_HANDLE getIndex);

    SoftKeyStore *next;
    CK_OBJECT_HANDLE index;
    Botan::Public_Key *botanKey;
};

#endif /* SOFTHSM_SOFTKEYSTORE_H */
