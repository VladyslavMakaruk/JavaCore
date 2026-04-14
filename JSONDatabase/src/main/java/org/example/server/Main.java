package org.example.server;

import java.io.IOException;

public class Main {
    static void main() {
        try {
            new Server(3333).Start();
        }catch (IOException e ){
            System.out.println(e.getMessage());
        }
    }
}
