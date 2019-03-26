package edu.saddleback.tictactoe.observable;

/**
 * Interface for the board listeners update method.
 */
public interface NameUpdatedListener{

    /**
     * Updates the listener with the current board.
     * @param newBoard
     */
    void update(boolean isPlayer1, String name);

}
