package edu.saddleback.tictactoe.decision;

import edu.saddleback.tictactoe.model.Board;

/**
 * Easy AI mode, chooses a random move to make against the user.
 */
public class RandomEvaluator implements StaticEvaluator{
    /**
     * Returns a random position on the board.
     * @param board
     * @return random board position.
     */
    @Override
    public int evaluate(Board board) {
        return (int)(Math.random()*11 - 5);
    }
}
