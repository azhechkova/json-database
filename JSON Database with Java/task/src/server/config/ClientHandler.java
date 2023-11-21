package server.config;

import common.RequestBody;
import common.ResponseBody;
import server.constants.Status;
import server.interfaces.DatabaseManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

class ClientHandler implements Callable<Boolean> {
    final Socket socket;
    private final DatabaseManager db;

    public ClientHandler(Socket s, DatabaseManager db) {
        this.socket = s;
        this.db = db;
    }

    @Override
    public Boolean call() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String request = in.readUTF();
            RequestBody body = RequestBody.fromJson(request);

            String response;
            boolean isExitRequest = body != null && body.getType().equals("exit");

            if (isExitRequest) {
                response = new ResponseBody(Status.OK.name(), null, null).toJson();
                out.writeUTF(response);
                in.close();
                out.close();
                return false;
            } else {
                response = db.parseRequest(body).toJson();
            }

            out.writeUTF(response);
            out.flush();

            System.out.println("Received: " + request);
            System.out.println("Sent: " + response);

            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}