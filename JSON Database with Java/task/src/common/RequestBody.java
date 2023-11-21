package common;

public class RequestBody implements JsonObject {
    private String type;
    private Object key;
    private Object value;

    public RequestBody(String method, Object key, Object value) {
        this.type = method;

        this.key = key;
        this.value = value;
    }

    public RequestBody(String method, String key) {
        this.type = method;
        this.key = key;
    }

    public RequestBody(String method) {
        this.type = method;
    }

    public static RequestBody fromJson(String json) {
        try {
            return GSON.fromJson(json, RequestBody.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String method) {
        this.type = method;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    };

    public String toJson() {
        return GSON.toJson(this);
    }
}
