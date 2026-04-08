package org.example.util;

import com.google.gson.*;

public class JsonConvertor {
    public static JsonArray getStringAsJsonArray(String input) {
        JsonElement key = new JsonPrimitive(input);
        if (key.isJsonArray()) {
            return key.getAsJsonArray();
        }
        if (key.isJsonPrimitive()) {
            String value = key.getAsString();

            try {
                JsonElement parsed = JsonParser.parseString(value);

                if (parsed.isJsonArray()) {
                    return parsed.getAsJsonArray();
                }
            } catch (Exception ignored) {
            }

            JsonArray arr = new JsonArray();
            arr.add(value);
            return arr;
        }
        JsonArray arr = new JsonArray();
        arr.add(key.toString());
        return arr;
    }

    public static JsonElement parseStringToJson(String input) {
        JsonElement rawValue = new JsonPrimitive(input);
        try {
            JsonElement parsed = JsonParser.parseString(input);
            if (parsed.isJsonObject() || parsed.isJsonArray()) {
                return parsed;
            }
        } catch (JsonSyntaxException ignored) {}

        return rawValue;
    }

    public static String elementToString(JsonElement element) {
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        }
        return element.toString();
    }
}
