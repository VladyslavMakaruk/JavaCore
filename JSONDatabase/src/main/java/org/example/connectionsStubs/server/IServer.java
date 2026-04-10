package org.example.connectionsStubs.server;

import org.example.connectionsStubs.Connection;

import java.io.IOException;
import java.net.SocketException;

public interface IServer extends AutoCloseable{
    Connection accept() throws IOException;
    void setSoTimeout(int timeout) throws SocketException;
    void close() throws IOException;
}
