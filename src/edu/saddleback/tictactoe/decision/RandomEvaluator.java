package edu.saddleback.tictactoe.decision;

import edu.saddleback.tictactoe.model.Board;

public class RandomEvaluator implements StaticEvaluator {
    @Override
    public int evaluate(Board board) {
        return (int)(Math.random()*11 - 5);
    }
}
