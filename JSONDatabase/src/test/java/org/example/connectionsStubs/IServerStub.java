package org.example.connectionsStubs;

import org.example.connectionsStubs.server.IServer;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class IServerStub implements IServer {

    private List<FakeConnection> connectionList = new ArrayList<>();
    int counter = 0;
    private int soTimeout = 5;

    public IServerStub(String[] commands) {
        for (String command : commands) {
            connectionList.add(new FakeConnection(command));
        }
    }

    @Override
    public synchronized Connection accept() throws IOException {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (counter < connectionList.size()) {
            return connectionList.get(counter++);
        }
        if (soTimeout > 0) {
            try {
                Thread.sleep(soTimeout);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            throw new SocketTimeoutException("Accept timed out");
        } else {
            throw new IOException("No more connections");
        }
    }

    @Override
    public void setSoTimeout(int timeout) throws SocketException {
        this.soTimeout = timeout;
    }

    @Override
    public void close() throws IOException {
        return;
    }

    public List<FakeConnection> returnConnectionList(){
        return this.connectionList;
    }
}
