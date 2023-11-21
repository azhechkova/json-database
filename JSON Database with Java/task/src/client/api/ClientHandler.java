package client.api;

import client.utils.CliArgs;
import com.beust.jcommander.JCommander;
import common.RequestBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

abstract class ClientHandler {
    private static final String PATH_TO_DIRECTORY = System.getProperty("user.dir");
    private static final String PATH_TO_DB = PATH_TO_DIRECTORY + "/src/client/data/";

    private static RequestBody getFromFile(String fileName) {
        try {
            String file = Files.readString(Path.of(PATH_TO_DB + fileName));
            return RequestBody.fromJson(file);
        } catch (IOException e) {
            return null;
        }
    }

    static RequestBody parseInput(String[] input) {
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

    abstract void start(String[] input);
}
