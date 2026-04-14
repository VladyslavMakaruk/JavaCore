package org.example.util;

import com.beust.jcommander.Parameter;
import com.google.gson.annotations.SerializedName;

public class Args {
    @Parameter(names = "-t")
    @SerializedName("type")
    private String commandType;

    @Parameter(names = "-k")
    @SerializedName("key")
    private String index;

    @Parameter(names = "-v")
    @SerializedName("value")
    private String message;

    @Parameter(names = "-in")
    private String fileName;

    public void transformCommandToLowerCase() {
        if (commandType != null) this.commandType = this.commandType.toLowerCase();
    }

    public String getCommandType() { return commandType; }
    public String getCommandKey() { return index; }
    public String getCommandMessage() { return message; }
    public String getFileName() { return fileName; }
    public void setCommandType(String commandType) { this.commandType = commandType; }
    public void setCommandKey(String index) { this.index = index; }
    public void setCommandMessage(String message) { this.message = message; }

}
