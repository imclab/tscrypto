package cl.niclabs.cb.dispatcher;

import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.methods.*;
import com.google.gson.Gson;

public class MethodDispatcher {

    public static String dispatch(String json, MethodFactory factory) {
        Gson gson = MethodParser.createGson(factory);
        return gson.toJson(gson.fromJson(json, Method.class).execute());
    }

}
