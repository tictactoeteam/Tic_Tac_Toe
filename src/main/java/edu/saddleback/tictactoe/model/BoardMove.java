package edu.saddleback.tictactoe.model;

import java.io.Serializable;

/**
 * Represents a board move, with the index and the correct piece used.
 */
public class BoardMove implements Serializable {
    private int row;
    private int col;
    GamePiece piece;

    /**
     * Constructor
     * @param row
     * @param col
     * @param piece
     */
    public BoardMove(int row, int col, GamePiece piece){
        this.row = row;
        this.col = col;
        this.piece = piece;
    }

    /**
     *Applys move to the given board.
     * @param board
     * @return
     * @throws GridAlreadyChosenException
     */
    public Board applyTo(Board board) throws GridAlreadyChosenException{
        board.set(row, col, piece);
        return board;
    }

    /**
     *Returns the board move from two given boards.
     * @param before
     * @param after
     * @return
     */
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
