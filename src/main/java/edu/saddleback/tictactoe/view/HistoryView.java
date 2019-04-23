package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.ServerConnection;

public class HistoryView {

    private ServerConnection conn;

    public void initialize(){

        conn = ServerConnection.getInstance();

    }

}
