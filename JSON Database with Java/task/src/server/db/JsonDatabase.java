package server.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.interfaces.Database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JsonDatabase implements Database {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String PATH_TO_DIRECTORY = System.getProperty("user.dir");
    private static final String PATH_TO_DB = PATH_TO_DIRECTORY + "/src/server/data/db.json";

    @Override
    public synchronized Object get(Object key) throws IllegalArgumentException {
        Object result;
        Map file = readFile();

        if (file == null) {
            throw new IllegalArgumentException("No database found");
        }

        if (key instanceof ArrayList<?>) {
            Iterator it = ((ArrayList<?>) key).iterator();
            result = file.get(it.next());

            while(it.hasNext()) {
                if (result instanceof Map<?, ?>) {
                    result = ((Map<?, ?>) result).get(it.next());
                } else {
                    throw new IllegalArgumentException("No such key");
                }
            }

        } else {
            result = file.get(key);
        }

        if (result == null) {
            throw new IllegalArgumentException("No such key");
        }

        return result;
    }

    private Map<String, Object> readFile() {
        try {
            String fileContent = Files.readString(Path.of(PATH_TO_DB));
            return GSON.fromJson(fileContent, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void writeInFile(Map<String, Object> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_TO_DB))) {
            writer.write(GSON.toJson(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setNestedValue(Map<String, Object> map, String[] keys, Object value) {
        Map<String, Object> currentMap = map;

        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            currentMap.putIfAbsent(key, new HashMap<>());
            currentMap = (Map<String, Object>) currentMap.get(key);
        }

        String lastKey = keys[keys.length - 1];
        currentMap.put(lastKey, value);
    }

    private static Object deleteNestedValue(Map<String, Object> map, String[] keys) {
        Map<String, Object> currentMap = map;

        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            currentMap.putIfAbsent(key, new HashMap<>());
            currentMap = (Map<String, Object>) currentMap.get(key);
        }

        String lastKey = keys[keys.length - 1];
        return currentMap.remove(lastKey);
    }

    @Override
    public synchronized void set(Object key, Object data) throws IllegalArgumentException {
        Map<String, Object> file = readFile();

        if (file == null) {
            file = new HashMap();
        }

        if (key instanceof ArrayList<?>) {
            String[] keyArray = ((ArrayList<?>) key).toArray(new String[0]);
            setNestedValue(file, keyArray, data);
        } else {
            file.put(key.toString(), data);
        }

        writeInFile(file);
    }

    @Override
    public synchronized void delete(Object key) throws IllegalArgumentException {
        Map file = readFile();

        if (file == null) {
            file = new HashMap();
        }
        Object deleted;

        if (key instanceof ArrayList<?>) {
            String[] keyArray = ((ArrayList<?>) key).toArray(new String[0]);
            deleted = deleteNestedValue(file, keyArray);
        } else {
            deleted = file.remove(key);
        }

        if (deleted == null) {
            throw new IllegalArgumentException("No such key");
        } else {
            writeInFile(file);
        }
    }
}
