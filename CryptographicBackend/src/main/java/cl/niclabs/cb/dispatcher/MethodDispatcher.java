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

package cl.niclabs.cb.dispatcher;

import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.methods.MethodFactory;
import com.google.gson.Gson;

public class MethodDispatcher {

    public static String dispatch(String json, MethodFactory factory) {
        Gson gson = MethodParser.createGson(factory);
        return gson.toJson(gson.fromJson(json, Method.class).execute());
    }

}
