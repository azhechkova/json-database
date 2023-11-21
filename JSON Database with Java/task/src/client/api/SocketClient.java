package client.api;

import common.Config;
import common.RequestBody;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClient extends ClientHandler {
    @Override
    public void start(String[] input) {
        try (Socket clientSocket = new Socket(InetAddress.getByName(Config.ADDRESS), Config.PORT);
             DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
            System.out.println("Client started!");


            RequestBody request = parseInput(input);
            String formatRequest = request.toJson();

            out.writeUTF(formatRequest);
            out.flush();

            String result = in.readUTF();

            System.out.println("Sent: " + formatRequest);
            System.out.println("Received: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
