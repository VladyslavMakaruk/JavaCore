package org.example.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Client(args,"localhost",3333).start();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
