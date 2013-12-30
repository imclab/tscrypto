-- Copyright 2013 NIC Chile Research Labs.
-- This file is part of PKCS11-TsCrypto.

-- PKCS11-TsCrypto is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Lesser General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- Foobar is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU Lesser General Public License for more details.

-- You should have received a copy of the GNU Lesser General Public License
-- along with Foobar.  If not, see <http://www.gnu.org/licenses/>.


DROP TABLE IF EXISTS TOKEN;
CREATE TABLE TOKEN (TKN_LABEL PRIMARY KEY, TKN_PIN TEXT, TKN_SO_PIN TEXT);

DROP TABLE IF EXISTS CRYPTO_OBJECT;
CREATE TABLE CRYPTO_OBJECT (TKN_LABEL TEXT, CO_HANDLE INTEGER, PRIMARY KEY (TKN_LABEL,  CO_HANDLE));

DROP TABLE IF EXISTS ATTRIBUTE;
CREATE TABLE ATTRIBUTE (TKN_LABEL TEXT,  CO_HANDLE INTEGER, ATT_TYPE INTEGER, ATT_VALUE BLOB, PRIMARY KEY (TKN_LABEL, CO_HANDLE, ATT_TYPE));

-- default token
INSERT INTO TOKEN VALUES ("TCBHSM", "1234", "1234"); 
