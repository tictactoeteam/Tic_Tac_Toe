package edu.saddleback.tictactoe.multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket server;
    private static final int PORT = 8765;

    public Server() {
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Unable to start server");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void start() {
        System.out.println("Starting server");
        while (true) {
            try {
                Socket socket = server.accept();
                Thread thread = new GameThread(socket);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
