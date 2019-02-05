package edu.saddleback.tictactoe.model;

import java.io.Serializable;

/**
 * Tracks the current state of the controller as an array of GamePieces and the current turnNumber
 */
public class Board implements Serializable {
    private GamePiece[][] board;
    private int turnNumber;

    public Board() {
        this.board = new GamePiece[3][3];
        this.turnNumber = 0;

        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 3; ++j){
                this.board[i][j] = null;
            }
        }
    }

    /**
     * Returns the game piece at the specified position
     * @param row the row of the game piece to return (0..2)
     * @param col the column of the game piece to return (0..2)
     * @return the game piece at position (row, col)
     */
    public GamePiece get(int row, int col) {
        return board[row][col];
    }

    /**
     * Returns the entire controller as a 3x3 array of GamePieces
     * @return the controller
     */
    public GamePiece[][] getBoard() {
        return board;
    }

    /**
     * Returns the number of moves that have been made by both players
     * @return the number of moves that have been made
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * Returns true if it is Player X's turn to play
     * @return true if Player X's turn, false otherwise
     */
    public boolean isXTurn() {
        return turnNumber % 2 == 0;
    }

    /**
     * Returns true if it is Player/CPU O's turn to play
     * @return true if Player/CPU O's turn, false otherwise
     */
    public boolean isOTurn() {
        return turnNumber % 2 == 1;
    }

    /**
     * Places a GamePiece at the specified position and increments the turn number. Does not overwrite a piece if one
     * already exists at the specified position
     * @param row the row to place the piece at
     * @param col the column to place the piece at
     * @param piece the piece to place
     * @throws GridAlreadyChosenException if there is already a piece at the specified position.
     */
    public void set(int row, int col, GamePiece piece) throws GridAlreadyChosenException {
        if (board[row][col] == null) {
            board[row][col] = piece;
            turnNumber++;
        } else {
            throw new GridAlreadyChosenException(row, col);
        }
    }
}
