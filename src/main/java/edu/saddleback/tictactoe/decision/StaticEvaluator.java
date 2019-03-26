package edu.saddleback.tictactoe.decision;
import edu.saddleback.tictactoe.model.Board;

/**
 * Interface for the different types of evaluators, easy or hard, that determine the next move for the computer to make.
 */
public interface StaticEvaluator{

    /**
     * Evaluates the next move from the current state of the board.
     * @param board
     * @return
     */
    int evaluate(Board board);

}
