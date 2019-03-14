package edu.saddleback.tictactoe.model;

import java.io.Serializable;

public class BoardMove implements Serializable {
    private int row;
    private int col;
    GamePiece piece;

    public BoardMove(int row, int col, GamePiece piece){
        this.row = row;
        this.col = col;
        this.piece = piece;
    }


    public void applyTo(Board board) throws GridAlreadyChosenException{
        board.set(row, col, piece);
    }

    public static BoardMove fromTwoBoards(Board before, Board after){
        for (int i=0; i<3; ++i){
            for (int j=0; j<3; ++j){
                if (before.get(i, j) == null && after.get(i, j) != null){
                    return new BoardMove(i, j, after.get(i, j));
                }
            }
        }
        return null;
    }

}
