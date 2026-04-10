package org.example.server;

import org.example.connectionsStubs.SocketConnection;
import org.example.connectionsStubs.Connection;
import org.example.connectionsStubs.server.IServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class IServerProd implements IServer {

    private final ServerSocket server;

    IServerProd(int port) throws IOException {
        this.server = new ServerSocket(port);
    }

    public Connection accept() throws IOException {
        return new SocketConnection(server.accept());
    }

    public void setSoTimeout(int timeout) throws SocketException {
        server.setSoTimeout(timeout);
    }

    public void close() throws IOException {
        this.server.close();
    }
}
