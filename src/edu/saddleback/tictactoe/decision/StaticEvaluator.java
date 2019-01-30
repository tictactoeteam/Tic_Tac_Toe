package edu.saddleback.tictactoe.decision;
import edu.saddleback.tictactoe.board.BoardController;

public interface StaticEvaluator {
    int evaluate(BoardController b);
}
