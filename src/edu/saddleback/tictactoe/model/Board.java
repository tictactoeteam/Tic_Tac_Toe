package edu.saddleback.tictactoe.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Tracks the current state of the controller as an array of GamePieces and the current turnNumber
 */
public class Board implements Serializable, Cloneable {
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
            turnNumber+= 1;
        } else {
            throw new GridAlreadyChosenException(row, col);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Turn #" + turnNumber + '\n');
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                char piece = board[i][j] == GamePiece.X ? 'X' : board[i][j] == GamePiece.O ? 'O' : ' ';
                sb.append(piece);
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        // no need to handle turn number, as that will always be the same for two equivalent arrangements of pieces
        return Arrays.deepHashCode(board);
    }

    @Override
    public Object clone(){
        Board copy = new Board();

        for (int i=0; i<3; ++i){
            for (int j=0; j<3; ++j){
                try {
                    if(get(i, j) != null)
                        copy.set(i, j, get(i, j));
                }catch(GridAlreadyChosenException ex){
                    // do absolutely nothing
                }
            }
        }

        return (Object)copy;
    }
}
