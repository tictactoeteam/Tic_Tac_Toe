package edu.saddleback.tictactoe;

import edu.saddleback.tictactoe.db.DbConnection;
import edu.saddleback.tictactoe.multiplayer.BetterServer;
import edu.saddleback.tictactoe.view.TicTacToeApplication;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    /**
     * Launches the application.
     * @param a
     */
    public static void main(String[] a){
        ArrayList<String> args = new ArrayList<>(Arrays.asList(a));

        if (args.contains("--migrate")) {
            DbConnection.runMigrations();
        }
        if (args.contains("--server")) {
            BetterServer server = new BetterServer();
            server.start();
        } else {
            Application.launch(TicTacToeApplication.class, a);
        }
    }
}
