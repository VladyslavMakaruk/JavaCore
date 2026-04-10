package org.example.connectionsStubs;

import java.io.IOException;

public interface Connection extends AutoCloseable {
    void send(String message) throws IOException;
    String receive() throws IOException;
    void close() throws IOException;
}
