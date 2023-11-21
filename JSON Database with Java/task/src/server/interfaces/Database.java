package server.interfaces;

public interface Database {
    void set(Object key, Object data);

    Object get(Object key);

    void delete(Object key);
}
