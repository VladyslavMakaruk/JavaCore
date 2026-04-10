package org.example.connectionsStubs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketConnection implements Connection {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public SocketConnection(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public SocketConnection(Socket socket) throws  IOException{
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void send(String message) throws IOException {
        out.writeUTF(message);
    }

    @Override
    public String receive() throws IOException {
        return in.readUTF();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}