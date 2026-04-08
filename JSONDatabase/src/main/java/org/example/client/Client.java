package org.example.client;
import com.beust.jcommander.JCommander;
import org.example.util.Args;
import com.google.gson.Gson;
import org.example.util.inputValidation.ArgsValidator;
import org.example.util.ReadArgsFromFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client {
    private Args parsedArgs = new Args();
    private final Gson jsonParser = new Gson();
    private final String serverIP;
    private final int serverPort;
    private final String defaultDirectoryPath = System.getProperty("user.dir") + "/src/client/data/";

    Client(String[] inputArgs, String serverIP, int ServerPort) throws IOException {
        this.serverIP = serverIP;
        this.serverPort = ServerPort;

        JCommander.newBuilder()
                .addObject(parsedArgs)
                .build()
                .parse(inputArgs);

        parsedArgs.transformCommandToLowerCase();
        System.out.println(parsedArgs.getCommandKey());
        createDirectory(defaultDirectoryPath);
    }

    public void start(){
        System.out.println("Client Started!");

        boolean argumentsIsValid = parseArgs();

        if (!argumentsIsValid){
            System.out.println("Wrong format of command arguments");
            return;
        }

        try (Socket socket = new Socket(serverIP,serverPort)) {
            handleRequest(new DataInputStream(socket.getInputStream()),
                    new DataOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createDirectory(String path) throws IOException {
        Path directoryPath = Path.of(path);
        if (!Files.exists(directoryPath)){
            Files.createDirectories(directoryPath);
        }
    }

    private void handleRequest(DataInputStream is, DataOutputStream os) throws IOException{
        String request = argToJson(parsedArgs);
        System.out.println("Sent: " + request);
        os.writeUTF(request);
        String result = is.readUTF();
        System.out.println("Received: " + result);
    }

    private String argToJson(Args arg){
        return jsonParser.toJson(arg);
    }

    private void parseArgsFromFile(String filename){
        try {
            var argsReader = new ReadArgsFromFile(filename);
            String jsonArguments = argsReader.argsInJsonFormat();
            parsedArgs = jsonParser.fromJson(jsonArguments, Args.class);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private boolean parseArgs(){
        if (parsedArgs.getFileName() != null && !parsedArgs.getFileName().isEmpty()){
            parseArgsFromFile(parsedArgs.getFileName());
        }

        return ArgsValidator.validateArgs(parsedArgs);
    }

}

