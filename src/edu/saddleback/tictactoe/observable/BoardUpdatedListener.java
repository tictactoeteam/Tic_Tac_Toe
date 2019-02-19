package edu.saddleback.tictactoe.observable;

import edu.saddleback.tictactoe.model.Board;

public interface BoardUpdatedListener {
    void update(Board newBoard);
}
