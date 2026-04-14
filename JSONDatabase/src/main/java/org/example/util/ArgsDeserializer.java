package org.example.util;
import com.google.gson.*;
import java.lang.reflect.Type;
import static org.example.util.JsonConvertor.elementToString;

public class ArgsDeserializer implements JsonDeserializer<Args> {
    @Override
    public Args deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Args args = new Args();
        if (obj.has("type")) {
            args.setCommandType(obj.get("type").getAsString());
        }
        if (obj.has("key")) {
            args.setCommandKey(elementToString(obj.get("key")));
        }
        if (obj.has("value")) {
            args.setCommandMessage(elementToString(obj.get("value")));
        }
        return args;
    }
}