package org.example.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class ServerResponse {

    public static String validResponse = "OK";
    public static String invalidResponse = "ERROR";
    private static final Gson jsonParser = new Gson();

    public static String serverResponseToJson(ServerResponse serverResponse){
        return jsonParser.toJson(serverResponse);
    }

    @SerializedName("response")
    String response;
    @SerializedName("reason")
    String reason;
    @SerializedName("value")
    JsonElement value;

    public ServerResponse(String response, JsonElement value, String reason){
        this.response = response;
        this.value = value;
        this.reason = reason;
    }
    public ServerResponse(String response, JsonElement value){
        this(response, value, null);
    }
    public ServerResponse(String response){
        this(response, null);
    }
    public String getResponse(){
        return this.response;
    }
}