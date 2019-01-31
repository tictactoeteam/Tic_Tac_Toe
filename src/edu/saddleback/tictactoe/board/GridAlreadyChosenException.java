package edu.saddleback.tictactoe.board;

/**
 * Custom exception that is triggered if either player tries to click a grid box that has already been chosen.
 */
public class GridAlreadyChosenException extends Exception {
    private int row, col;
    public GridAlreadyChosenException(int row, int col, String errorMessage){
        super(errorMessage);
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
