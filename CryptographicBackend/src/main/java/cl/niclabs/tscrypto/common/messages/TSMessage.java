/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.tscrypto.common.messages;

import cl.niclabs.tscrypto.common.datatypes.JsonFormat;
import cl.niclabs.tscrypto.common.utils.Util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Threshold Cryptography abstract message.
 * Composed of: type, version, created_at, payload (useful data to be defined by inherited types)
 * @author acansado
 *
 */
public abstract class TSMessage extends JsonFormat {

	String type;
	String version;
	long createdAt;

	public TSMessage() {
	}

	public TSMessage(String type, String version) {
		this.type = type;
		this.version = version;
		this.createdAt = System.currentTimeMillis();
	}
	
    @SuppressWarnings("unchecked")
	public static <T> T fromJson(String jsonText) throws JsonParseException {

        Gson gson = Util.GsonFactory(true);

        return (T) gson.fromJson(jsonText, TSMessage.class);
    }

	/**
	 * type getter
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * createdAt getter
	 * @return
	 */
	public long getCreatedAt(){
		return createdAt;
	}
}
