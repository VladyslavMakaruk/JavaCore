package org.example.connectionsStubs.client;

import org.example.connectionsStubs.Connection;

import java.io.IOException;

public interface ConnectionFactory {
    Connection create(String host, int port) throws IOException;
}
