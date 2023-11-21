package common;

import server.constants.Status;

public class ResponseBody implements JsonObject {
    private String response;
    private Object value;
    private String reason;

    public ResponseBody(String response, Object value, String reason) {
        this.response = response;
        this.value = value;
        this.reason = reason;
    }

    public ResponseBody() {}

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static ResponseBody success(Object value) {
        return new ResponseBody(Status.OK.name(), value, null);
    }

    public static ResponseBody error(String reason) {
        return new ResponseBody(Status.ERROR.name(), null, reason);
    }

    public String toJson() {
        return GSON.toJson(this);
    };

    public static ResponseBody fromJson(String json) {
        try {
            return GSON.fromJson(json, ResponseBody.class);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
