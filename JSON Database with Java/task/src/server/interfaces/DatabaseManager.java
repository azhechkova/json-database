package server.interfaces;

import common.RequestBody;
import common.ResponseBody;

public interface DatabaseManager {
    ResponseBody parseRequest(RequestBody body);
    void validateBody(RequestBody body);
}
