package cl.niclabs.cb.dispatcher;

import cl.niclabs.cb.backend.methods.*;
import com.google.gson.Gson;

import cl.niclabs.cb.backend.methods.FindKeyMethod;
import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.ResponseMessage;
import com.google.gson.GsonBuilder;

public class MethodDispatcher {

    public static String dispatch(String json, MethodFactory factory) {
        Gson gson = MethodMessageParser.createGson(factory);
        Method method = gson.fromJson(json, Method.class);
        return gson.toJson(method.execute());
    }

}
