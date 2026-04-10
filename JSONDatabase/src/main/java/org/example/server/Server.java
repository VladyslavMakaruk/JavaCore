package org.example.server;
import com.google.gson.*;
import org.example.connectionsStubs.Connection;
import org.example.connectionsStubs.server.IServer;
import org.example.connectionsStubs.server.IServerFactory;
import org.example.database.JSONDatabase;
import org.example.util.Args;
import org.example.util.ArgsDeserializer;
import org.example.util.ServerResponse;
import org.example.util.inputValidation.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import static org.example.util.JsonConvertor.*;

public class Server {
    private final Gson jsonParser = new GsonBuilder()
            .registerTypeAdapter(Args.class, new ArgsDeserializer())
            .create();
    private final JSONDatabase database;
    private final int PORT;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private boolean shutDownCondition = false;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReadWriteLock shutDownLock = new ReentrantReadWriteLock();
    private final IServerFactory serverFactory;

    public Server(int port) throws IOException {
        this.database = new JSONDatabase();
        this.PORT = port;
        this.serverFactory = IServerProd::new;
    }

    Server(String filePath, String fileName, int port, IServerFactory serverFactory) throws IOException {
        this.database = new JSONDatabase(filePath,fileName);
        this.PORT = port;
        this.serverFactory = serverFactory;
    }

    public void Start(){
        try(IServer server = serverFactory.create(PORT)){
            System.out.println("Server started!");
            while (!isShutDown()){
                try {
                    server.setSoTimeout(500);
                    Connection connection = server.accept();
                    executor.submit(() -> {
                        try {
                            handleConnection(connection);
                        } catch (Exception e) {
                            try {
                                connection.close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            System.out.println(e.getMessage());
                        }
                    });
                } catch (java.net.SocketTimeoutException e){
                    continue;
                } catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage() );
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    private void handleConnection(Connection connection) {
        try {
            Args request = jsonParser.fromJson(connection.receive(), Args.class);
            ServerResponse response;
            if(!ArgsValidator.validateArgs(request)){
                response = new ServerResponse(ServerResponse.invalidResponse,null,"invalid request");
                connection.send(ServerResponse.serverResponseToJson(response));
            } else {
                response =  handleRequest(request);
                connection.send(ServerResponse.serverResponseToJson(response));
            }
        } catch (IOException e) {
            System.out.println("Error while handling request: " + e.getMessage());
        }

    }

    ServerResponse handleRequest(Args request){
        for (Command command : Command.values()) {
            Matcher matcher = command.getPattern().matcher(request.getCommandType().trim());
            if (matcher.matches()){
                switch (command) {
                    case GET-> {
                        return handleGet(request);
                    }
                    case DELETE -> {
                        return handleDelete(request);
                    }
                    case SET -> {
                        return handleSet(request);
                    }
                    case EXIT -> {
                        return handleExit();
                    }
                }
            }
        }
        throw new RuntimeException("invalid command type");
    }

    private ServerResponse handleSet(Args request){
        rwLock.writeLock().lock();
        JsonElement value = parseStringToJson(request.getCommandMessage());
        try {
            database.setByPath(getStringAsJsonArray(request.getCommandKey()),value);
            return new ServerResponse(ServerResponse.validResponse);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private ServerResponse handleGet(Args request){
        rwLock.readLock().lock();
        try {
            JsonElement result = database.getByJsonArray(getStringAsJsonArray(request.getCommandKey()));
            if (result == null) {
                return new ServerResponse(ServerResponse.invalidResponse,null, JSONDatabase.noKey);
            } else {
                return new ServerResponse(ServerResponse.validResponse,result);
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

    private ServerResponse handleDelete(Args request){
        rwLock.writeLock().lock();
        try {
            if(database.deleteByPath(getStringAsJsonArray(request.getCommandKey()))){
                return  new ServerResponse(ServerResponse.validResponse);
            } else {
                return new ServerResponse(ServerResponse.invalidResponse,null,"No such key");
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private ServerResponse handleExit(){
        shutDownLock.writeLock().lock(); //TODO: additional lock?
        try {
            shutDownCondition = true;
            return new ServerResponse(ServerResponse.validResponse);
        } finally {
            shutDownLock.writeLock().unlock();
        }
    }

    boolean isShutDown(){
        shutDownLock.readLock().lock();
        try {
            return shutDownCondition;
        } finally {
            shutDownLock.readLock().unlock();
        }
    }


}