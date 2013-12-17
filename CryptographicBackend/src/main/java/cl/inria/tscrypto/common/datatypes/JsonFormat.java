package cl.inria.tscrypto.common.datatypes;

import cl.inria.tscrypto.common.utils.Util;

import com.google.gson.Gson;

public class JsonFormat {
    public String toJson(boolean pretty) {
    	Gson gson = Util.GsonFactory(pretty);
        return gson.toJson(this);
    }

    public String toJsonPretty() {
        return toJson(true);
    }
    
    public String toJson() {
        return toJson(false);
    }

}
