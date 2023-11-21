package server;

import server.config.SocketServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SocketServer server = new SocketServer();
        server.start();
    }
}
