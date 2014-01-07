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

package cl.niclabs.tscrypto.common.datatypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONData implements Data {

	private JSONObject jsonObject;

	public JSONData(String data) {
		try {
			this.jsonObject = new JSONObject(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public JSONData() {
		this.jsonObject = new JSONObject();
	}

	public JSONData(File file) throws IOException {
		this(readFile(file));
	}

	private static String readFile(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));

		StringBuffer strBuffer = new StringBuffer();
		String line = null;
		while ((line = bf.readLine()) != null)
			strBuffer.append(line);
		return strBuffer.toString();
	}

	public String getString(String key) {
		String value = null;
		try {
			value = jsonObject.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;

	}

	public long getLong(String key) {
		long value = 0;
		try {
			value = jsonObject.getLong(key);
		} catch (JSONException e) {
			// TODO: Change this and put it into the log file
			System.err.println("Attribute doesn't exist: " + key);
			value = -1;
		}
		return value;

	}

	public int getInt(String key) {
		int value = 0;
		try {
			value = jsonObject.getInt(key);
		} catch (JSONException e) {
			// TODO: Change this and put it into the log file
			System.err.println("Attribute doesn't exist: " + key);
			value = -1;
		}
		return value;
	}

	public Data getData(String key)
	{
		Data value = null;
		try {
			// TODO: improve this reification
			value = new JSONData(jsonObject.getJSONObject(key).toString());
		} catch (JSONException e) {
			// TODO: Change this and put it into the log file
			System.err.println("Attribute doesn't exist: " + key);
			value = null;
		}
		return value;
	}

	public void put(String key, Object value) {
		try {
			if (value instanceof JSONData) {
				// TODO: this is just a quick fix
				jsonObject.put(key, ((JSONData) value).jsonObject);
			} else {
				// to save a list JSONData as object instead of a String 
				if(value instanceof JSONData[]){
					
					for (JSONData val : (JSONData[])value) {
						System.out.println("Pase");
						jsonObject.put(key, ((JSONData) val).jsonObject);
					}
					
				}
				
				jsonObject.put(key, value);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString() {
		return jsonObject.toString();
	}

}
