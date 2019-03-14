package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;

public class HumanPlayer extends Player{

    public HumanPlayer(GameController hope){
        this(hope, "127.0.0.1");
    }

    public HumanPlayer(GameController hope, String IP){
        this(hope, IP, 6969);
    }

    public HumanPlayer(GameController hope, String IP, int port){
        super(hope, IP, port);

        behavior = new Thread(() -> {
            while(winnerChecker.evaluate(hope.getBoard()) == 0 || hope.getBoard().getTurnNumber() < 9) {
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

    @Override
    public void setMove(int row, int col, GamePiece piece){
        boardMove = new BoardMove(row, col, piece);
    }
}
