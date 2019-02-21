package edu.saddleback.tictactoe.observable;

import edu.saddleback.tictactoe.model.Board;

/**
 * Interface for the board listeners update method.
 */
public interface BoardUpdatedListener{

    /**
     * Updates the listener with the current board.
     * @param newBoard
     */
    void update(Board newBoard);

}
