package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;

public class HumanPlayer extends Player{

    public HumanPlayer(GameController hope){
        this(hope, false);
    }

    public HumanPlayer(GameController hope, boolean isPlayer1) {
        this(hope, isPlayer1, "127.0.0.1");
    }

    public HumanPlayer(GameController hope, boolean isPlayer1, String IP){
        this(hope, isPlayer1, IP, 6969);
    }

    public HumanPlayer(GameController hope, boolean isPlayer1, String IP, int port){
        super(hope, isPlayer1, IP, port);

        behavior = new Thread(() -> {
            if (this.isPlayer1()) {
                connection.sendName(hope.getPlayer1Name());
            } else {
                connection.sendName(hope.getPlayer2Name());
            }
            readName();
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
