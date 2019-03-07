package edu.saddleback.tictactoe.multiplayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    ServerSocket server;
    private static final int PORT = 6969;
    private InetAddress PlayerX;
    private InetAddress PlayerO;
    private boolean threadIsGoodAndRunning = true;

    GameThread thread;

    private Thread serverTask = new Thread(() -> {
        System.out.println("Starting server");
        while (threadIsGoodAndRunning) {
            try {
                System.out.println("Awaiting first player for game no.1");
                Socket socketPlayerX = server.accept();
                PlayerX = socketPlayerX.getInetAddress();
                System.out.println("Player X joined the game! The Challenger's IP address is: " + PlayerX.getHostAddress());

                System.out.println("Awaiting second player for game no.1");
                Socket socketPlayerO = server.accept();
                PlayerO = socketPlayerO.getInetAddress();
                System.out.println("Player O joined the game! The Challenger's IP address is: " + PlayerO.getHostAddress());

                System.out.println("Let the game begin!!");
                thread = new GameThread(socketPlayerX, socketPlayerO);
                //Sessions.add(thread);

                thread.start();

            } catch (IOException e) {
                System.out.println("Connection closed, game ended!");
            }
        }
    });

    //LinkedList<GameThread> Sessions = new LinkedList<>();

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
        serverTask.start();
    }

    public void stop(){
        threadIsGoodAndRunning = false;
        thread.cease();
    }
}
