package server.config;

import common.Config;
import server.db.JsonDatabaseManager;
import server.interfaces.DatabaseManager;
import server.interfaces.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer implements Server {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DatabaseManager db = new JsonDatabaseManager();

    @Override
    public void start() throws IOException {
        serverSocket = new ServerSocket(Config.PORT, 50, InetAddress.getByName(Config.ADDRESS));

        System.out.println("Server started!");
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        executor.submit(() -> {
            while (true) {

                try {
                    clientSocket = serverSocket.accept();

                    var handler = new ClientHandler(clientSocket, db);
                    Boolean result = handler.call();

                    if (!result) {
                        this.serverSocket.close();
                        this.clientSocket.close();
                        executor.shutdownNow();
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("ERROR IN RUNNING: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void stop() {
        try {
            executorService.shutdownNow();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


