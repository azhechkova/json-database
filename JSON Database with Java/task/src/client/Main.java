package client;

import client.api.SocketClient;

public class Main {
    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        client.start(args);
    }
}
