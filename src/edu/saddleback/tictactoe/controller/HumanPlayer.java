package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.model.Board;

public class HumanPlayer extends Player{

    public HumanPlayer(Board board){
        this(board, "127.0.0.1");
    }

    public HumanPlayer(Board board, String IP){
        this(board, IP, 6969);
    }

    public HumanPlayer(Board board, String IP, int port){
        super(board, IP, port);

        behavior = new Thread(() -> {
            while(winnerChecker.evaluate(board) == 0 || board.getTurnNumber() !=9) {
                readBoard();
                boardMove = null;
                try {
                    while (boardMove == null) {
                        Thread.sleep(50);
                    }
                } catch (InterruptedException ex) {
                }
                sendMove();
                boardMove = null;
            }
        });
    }
}
