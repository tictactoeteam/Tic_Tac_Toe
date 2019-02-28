package edu.saddleback.tictactoe.multiplayer;

import java.net.Socket;

public class GameThread extends Thread {
    private Socket socket;

    public GameThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public synchronized void start() {
        super.start();
    }
}
