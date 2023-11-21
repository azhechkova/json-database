package client.api;

import client.utils.CliArgs;
import com.beust.jcommander.JCommander;
import common.Config;
import common.RequestBody;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class SocketClient {
    private static final String PATH_TO_DIRECTORY = System.getProperty("user.dir");
    private static final String PATH_TO_DB = PATH_TO_DIRECTORY + "/src/client/data/";

    // FOR DEVELOPMENT
//    private static final String PATH_TO_DB = PATH_TO_DIRECTORY + "/JSON Database with Java/task/src/client/data/";

    private static RequestBody getFromFile(String fileName) {
        try {
            String file = Files.readString(Path.of(PATH_TO_DB + fileName));
            return RequestBody.fromJson(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static RequestBody parseInput(String[] input) {
        try {
            CliArgs args = new CliArgs();

            JCommander.newBuilder()
                    .addObject(args)
                    .build()
                    .parse(input);

            if (args.getFileName() != null) {
                return getFromFile(args.getFileName());
            }

            String method = args.getMethod();
            String key = args.getKey();

            RequestBody body = new RequestBody(method, key);

            if (method.equals("set")) {
                body.setValue(args.getValue());
            }

            return body;
        } catch (Exception e) {
            return new RequestBody("get", "-1");
        }
    }

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
