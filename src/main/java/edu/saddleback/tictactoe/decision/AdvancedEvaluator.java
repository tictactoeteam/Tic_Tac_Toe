package edu.saddleback.tictactoe.decision;

import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;

/**
 * Used for the hard mode AI to choose the best move given the current status of the game board.
 */
public class AdvancedEvaluator implements StaticEvaluator {

    /**
     * Used in minimax to calculate the "choice score" of each given possible move.
     * @param board
     * @return The
     */
    private int score(Board board){
        int sumX;
        int sumO;
        for (int i=0; i<3; ++i){
            sumO = 0;
            sumX = 0;
            for (int j=0; j<3; ++j){
                if (board.get(i, j) == GamePiece.X)
                    sumX ++;
                if (board.get(i, j) == GamePiece.O)
                    sumO ++;
            }
            if (sumX == 3)
                return 20;
            if (sumO == 3)
                return -20;
        }

        for (int i=0; i<3; ++i){
            sumO = 0;
            sumX = 0;
            for (int j=0; j<3; ++j){
                if (board.get(j, i) == GamePiece.X)
                    sumX ++;
                if (board.get(j, i) == GamePiece.O)
                    sumO ++;
            }
            if (sumX == 3)
                return 20;
            if (sumO == 3)
                return -20;
        }


        sumO = 0;
        sumX = 0;
        for (int i=0; i<3; ++i){
            if (board.get(i, i) == GamePiece.X)
                sumX ++;
            if (board.get(i, i) == GamePiece.O)
                sumO ++;
        }
        if (sumX == 3)
            return 20;
        if (sumO == 3)
            return -20;

        sumO = 0;
        sumX = 0;
        for (int i=0; i<3; ++i){
            if (board.get(i, 2-i) == GamePiece.X)
                sumX ++;
            if (board.get(i, 2-i) == GamePiece.O)
                sumO ++;
        }
        if (sumX == 3)
            return 20;
        if (sumO == 3)
            return -20;



        return 0;

    }

    /**
     *
     * @param board
     * @return The total number of possible moves left on the current board.
     */
    private int depth(Board board){
        int depth = 0;
        for (int i=0; i<3; ++i){
            for (int j=0; j<3; ++j){
                if (board.get(i, j) != null)
                    depth++;
            }
        }

        return depth;
    }


    /**
     * Function evaluates every non-winning position to 0, and a winning position to +-(10-depth) depending on whether
     * X or O is winning
     * @param board
     * @return int score
     */
    @Override
    public int evaluate(Board board){
        int S = score(board);

        if (S==0)
            return 0;
        if (S >0)
            return S-depth(board);
        else
            return depth(board)+S;
    }
}
