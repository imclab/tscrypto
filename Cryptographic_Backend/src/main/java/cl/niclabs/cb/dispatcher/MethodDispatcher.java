package cl.niclabs.cb.dispatcher;

import cl.niclabs.cb.backend.methods.*;
import com.google.gson.Gson;

import cl.niclabs.cb.backend.methods.FindKeyMethod;
import cl.niclabs.cb.backend.Method;
import cl.niclabs.cb.backend.ResponseMessage;
import com.google.gson.GsonBuilder;

public class MethodDispatcher {

    private final Method method;

    public MethodDispatcher(String methodJson, MethodFactory factory) {
        Gson gson = MethodMessageParser.createGson(factory);
        method = gson.fromJson(methodJson, Method.class);
    }

    public String dispatch() {
        Gson gson = new Gson();
        ResponseMessage rm = method.execute();
        return gson.toJson(rm);
    }
}
