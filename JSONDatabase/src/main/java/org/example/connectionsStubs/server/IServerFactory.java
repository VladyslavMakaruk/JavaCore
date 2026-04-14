package org.example.connectionsStubs.server;

import java.io.IOException;

public interface IServerFactory {
    IServer create(int port) throws IOException;
}
