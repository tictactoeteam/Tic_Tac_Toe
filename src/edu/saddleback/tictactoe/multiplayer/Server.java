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

    //We need to check if the save file exists when we start this thread.. the board is saved, however it is never loaded
    //The board needs to be loaded to the local version of the game when/if a game save already exists
    private Thread serverTask = new Thread(() -> {
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

                thread.start();

            } catch (IOException e) {
                System.out.println("Connection closed, game ended!");
            }

            System.out.println("I made it out of thread too");


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
        try {
            server.close();
        }catch(Exception ex){}
    }
}
