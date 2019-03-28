package edu.saddleback.tictactoe;

import edu.saddleback.tictactoe.multiplayer.BetterServer;
import edu.saddleback.tictactoe.view.TicTacToeApplication;
import javafx.application.Application;

public class Main {
    /**
     * Launches the application.
     * @param args
     */
    public static void main(String[] args){
        if (args.length > 0 && args[0].equals("--server")) {
            BetterServer server = new BetterServer();
            server.fireTheServer();
        } else {
            Application.launch(TicTacToeApplication.class, args);
        }
    }
}
