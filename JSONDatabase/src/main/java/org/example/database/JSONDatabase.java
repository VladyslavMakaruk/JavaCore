package org.example.database;
import com.google.gson.*;
import java.io.*;
import java.nio.file.*;

public class JSONDatabase {

    public static String noKey = "No such key";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String filePath;
    private final JsonObject data;
    static final String defaultFileName = "db.json";
    static final String defaultFilePath = System.getProperty("user.dir") + "/src/client/data/";

    public JSONDatabase(String filepath, String filename) throws IOException {
        this.filePath = filepath + filename;
        this.data = load();
        this.save();
    }

    public JSONDatabase() throws IOException {
        this(defaultFilePath,defaultFileName);
    }

    private JsonObject load() throws IOException {
        if (!Files.exists(Path.of(filePath))) {
            return new JsonObject();
        }
        try (Reader reader = Files.newBufferedReader(Path.of(filePath))) {
            JsonElement el = JsonParser.parseReader(reader);
            return el.isJsonNull() ? new JsonObject() : el.getAsJsonObject();
        }
    }

    private void save() throws IOException {
        try (Writer writer = Files.newBufferedWriter(Path.of(filePath))) {
            gson.toJson(data, writer);
        }
    }

    public void setByPath(JsonArray path, JsonElement value) {
        JsonObject current = data;
        for (int i = 0; i < path.size() - 1; i++) {
            String key = path.get(i).getAsString();

            if (!current.has(key) || !current.get(key).isJsonObject()) {
                current.add(key, new JsonObject());
            }
            current = current.getAsJsonObject(key);
        }
        String lastKey = path.get(path.size() - 1).getAsString();
        current.add(lastKey, value);
    }

    public boolean deleteByPath(JsonArray path) {
        JsonObject current = data;
        for (int i = 0; i < path.size() - 1; i++) {
            String key = path.get(i).getAsString();

            if (!current.has(key) || !current.get(key).isJsonObject()) {
                return false;
            }
            current = current.getAsJsonObject(key);
        }
        var property = path.get(path.size() - 1).getAsString();
        if (current.has(property)) {
            current.remove(property);
            return true;
        }
        return false;
    }

    public JsonElement getByJsonArray(JsonArray path) {
        JsonElement current = data;
        for (JsonElement keyElement : path) {
            String key = keyElement.getAsString();
            if (!current.isJsonObject()) {
                return null;
            }
            current = current.getAsJsonObject().get(key);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

}