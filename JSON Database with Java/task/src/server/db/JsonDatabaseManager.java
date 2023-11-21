package server.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.RequestBody;
import common.ResponseBody;
import server.constants.Method;
import server.interfaces.Database;
import server.interfaces.DatabaseManager;

public class JsonDatabaseManager implements DatabaseManager {
    private Database db;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public JsonDatabaseManager() {
        this.db = new JsonDatabase();
    }

    @Override
    public void validateBody(RequestBody body) throws IllegalArgumentException {
        if (body == null) {
            throw new IllegalArgumentException("Body should be present");
        }

        Method dbMethod = Method.getByName(body.getType());

        if (dbMethod == null || dbMethod == Method.EXIT) {
            throw new IllegalArgumentException("No such method");
        }

        if (dbMethod == Method.SET && body.getValue() == null) {
            throw new IllegalArgumentException("Body is required");
        }
    }

    @Override
    public ResponseBody parseRequest(RequestBody body) {
        try {
            validateBody(body);

            Method dbMethod = Method.getByName(body.getType());

            if (dbMethod == null) {
                return ResponseBody.error("No such method");
            }

            if (dbMethod == Method.EXIT) {
                return null;
            }

            Object key = body.getKey();
            Object result = null;

            switch (dbMethod) {
                case GET -> {
                    result = db.get(key);
                }
                case SET -> db.set(key, body.getValue());
                case DELETE -> db.delete(key);
            };

            return ResponseBody.success(result);
        } catch (IllegalArgumentException e) {
            return ResponseBody.error(e.getMessage());
        }
    }
}
