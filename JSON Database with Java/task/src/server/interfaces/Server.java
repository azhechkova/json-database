package server.interfaces;

import java.io.IOException;

public interface Server {
    void start() throws IOException;

    void stop();
}
