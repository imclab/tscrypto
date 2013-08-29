/* $Id: checks.h 6607 2012-09-04 12:09:15Z rb $ */

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

#define NULL_PTR ((void*) 0)
#define MIN_PIN_LEN 3
#define MAX_PIN_LEN 10

void usage();
void inittoken();
void runInitCheck(unsigned int counter);
void runInfoCheck(unsigned int counter);
void runSessionCheck(unsigned int counter);
void runUserCheck(unsigned int counter);
void runRandomCheck(unsigned int counter);
void runGenerateCheck(unsigned int counter);
void runObjectCheck(unsigned int counter);
void runDigestCheck(unsigned int counter);
void runSignCheck(unsigned int counter);
void runVerifyCheck(unsigned int counter);
void runEncryptCheck(unsigned int counter);
void runDecryptCheck(unsigned int counter);
void runSignVerifyCheck();
