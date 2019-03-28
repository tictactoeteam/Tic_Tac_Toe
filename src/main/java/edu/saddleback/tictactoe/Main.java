package edu.saddleback.tictactoe;

import edu.saddleback.tictactoe.multiplayer.Server;
import edu.saddleback.tictactoe.view.TicTacToeApplication;
import javafx.application.Application;

public class Main {
    /**
     * Launches the application.
     * @param args
     */
    public static void main(String[] args){
        if (args.length > 0 && args[0].equals("--server")) {
            Server server = new Server();
            server.start();
        } else {
            Application.launch(TicTacToeApplication.class, args);
        }
    }
}
