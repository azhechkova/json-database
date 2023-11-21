package common;

import com.google.gson.Gson;

interface JsonObject {
    public static final Gson GSON = new Gson();

    String toJson();
}
