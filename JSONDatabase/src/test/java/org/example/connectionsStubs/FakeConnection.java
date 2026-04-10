package org.example.connectionsStubs;

public class FakeConnection implements Connection {
    private String lastSentMessage;
    private final String responseToReturn;

    public FakeConnection(String responseToReturn) {
        this.responseToReturn = responseToReturn;
    }

    @Override
    public void send(String message) {
        this.lastSentMessage = message;
    }

    @Override
    public String receive() {
        return responseToReturn;
    }

    @Override
    public void close() {
        System.out.println("connection stopped");
    }

    public String getLastSentMessage() {
        return lastSentMessage;
    }
}