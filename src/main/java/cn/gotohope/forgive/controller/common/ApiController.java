package cn.gotohope.forgive.controller.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiController {

    protected String ok() {
        return ok(null);
    }

    protected String ok(String msg) {
        JsonObject object = new JsonObject();
        object.addProperty("error", 0);
        if (msg != null && !msg.isEmpty())
            object.addProperty("msg", msg);
        return object.toString();
    }

    protected String error(int errCode, String errMsg) {
        JsonObject object = new JsonObject();
        object.addProperty("error", errCode);
        object.addProperty("msg", errMsg);
        return object.toString();
    }

    protected JsonObject json_decode(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

}
