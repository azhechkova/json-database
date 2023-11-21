package server.constants;

import java.util.Objects;

public enum Method {
    GET("get"),
    SET("set"),
    DELETE("delete"),
    EXIT("exit");

    private final String name;

    Method(String name) {
        this.name = name;
    }

    public static Method getByName(String name) {
        Method result = null;
        Method[] values = Method.values();

        for (Method value : values) {
            if (Objects.equals(value.getName(), name)) {
                result = value;
                break;
            }
        }

        return result;
    }

    public String getName() {
        return name;
    }
}
