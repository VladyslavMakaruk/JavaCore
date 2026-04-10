package org.example.client;
import com.beust.jcommander.JCommander;
import org.example.connectionsStubs.SocketConnection;
import org.example.connectionsStubs.client.ConnectionFactory;
import org.example.connectionsStubs.Connection;
import org.example.util.Args;
import com.google.gson.Gson;
import org.example.util.ReadFromFile;
import org.example.util.inputValidation.ArgsValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client {
    private Args parsedArgs = new Args();
    private final Gson jsonParser = new Gson();
    private final String serverIP;
    private final int serverPort;
    public static String defaultDirectoryPath = System.getProperty("user.dir") + "/src/client/data/";
    private final String directoryPath;
    private final ConnectionFactory conFactory;


    Client(String[] inputArgs, String serverIP, int ServerPort,String directoryPath, ConnectionFactory conFactory) throws IOException {
        this.serverIP = serverIP;
        this.serverPort = ServerPort;
        this.directoryPath = directoryPath;
        this.conFactory = conFactory;

        JCommander.newBuilder()
                .addObject(parsedArgs)
                .build()
                .parse(inputArgs);

        parsedArgs.transformCommandToLowerCase();
        createDirectory(directoryPath);
    }

    Client(String[] inputArgs, String serverIP, int ServerPort) throws IOException {
        this(inputArgs,serverIP,ServerPort,defaultDirectoryPath, SocketConnection::new);
    }

    public void start(){
        boolean argumentsIsValid = parseArgs();
        if (!argumentsIsValid){
            System.out.println("Wrong format of command arguments");
            return;
        }

        try (Connection connection = conFactory.create(serverIP,serverPort)) {
            handleRequest(connection);
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

    private void handleRequest(Connection connection) throws IOException{
        String request = argToJson(parsedArgs);
        System.out.println("Sent: " + request);
        connection.send(request);
        String result = connection.receive();
        System.out.println("Received: " + result);
    }

    private String argToJson(Args arg){
        return jsonParser.toJson(arg);
    }

    private void parseArgsFromFile(String filename){
        try {
            String jsonArguments = ReadFromFile.readString(directoryPath,filename);
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

