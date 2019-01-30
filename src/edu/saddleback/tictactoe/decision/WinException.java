package edu.saddleback.tictactoe.decision;

import edu.saddleback.tictactoe.Board;

public class WinException extends Exception {
    private Board.Piece winner;

    public WinException(Board.Piece piece){
        winner = piece;
    }

    public Board.Piece getWinner(){
        return winner;
    }
}
