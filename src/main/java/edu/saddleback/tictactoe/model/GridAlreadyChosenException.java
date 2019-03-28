package edu.saddleback.tictactoe.model;

/**
 * Custom exception that is triggered if either player tries to click a grid box that has already been chosen.
 */
public class GridAlreadyChosenException extends Exception{

    private int row, col;

    /**
     * Constructs this expection object with the necessary data.
     * @param row
     * @param col
     */
    public GridAlreadyChosenException(int row, int col){
        super("Position " + row + ", " + col + " already has a GamePiece.");
        this.row = row;
        this.col = col;
    }

}
