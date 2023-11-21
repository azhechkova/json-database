package server.interfaces;

import com.google.gson.JsonObject;

public interface Database {
    void set(Object key, Object data);

    Object get(Object key);

    void delete(Object key);
}
