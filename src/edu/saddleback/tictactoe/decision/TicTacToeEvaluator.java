package edu.saddleback.tictactoe.decision;

import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;

public class TicTacToeEvaluator implements StaticEvaluator {

    private int score(Board board){
        int numStreaks = 0;

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
                return 100;
            if (sumX == 2)
                if (sumO == 0)
                    numStreaks++;
            if (sumO == 3)
                return -100;
            if (sumO == 2)
                if (sumX == 0)
                    numStreaks--;
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
                return 100;
            if (sumX == 2)
                if (sumO == 0)
                    numStreaks++;
            if (sumO == 3)
                return -100;
            if (sumO == 2)
                if (sumX == 0)
                    numStreaks--;
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
            return 100;
        if (sumX == 2)
            if (sumO == 0)
                numStreaks++;
        if (sumO == 3)
            return -100;
        if (sumO == 2)
            if (sumX == 0)
                numStreaks--;

        sumO = 0;
        sumX = 0;
        for (int i=0; i<3; ++i){
            if (board.get(i, 2-i) == GamePiece.X)
                sumX ++;
            if (board.get(i, 2-i) == GamePiece.O)
                sumO ++;
        }
        if (sumX == 3)
            return 100;
        if (sumX == 2)
            if (sumO == 0)
                numStreaks++;
        if (sumO == 3)
            return -100;
        if (sumO == 2)
            if (sumX == 0)
                numStreaks--;



        return numStreaks;

    }

    /**
     * Function counts the number of streaks, returns it, or +-100 if we have the winner
     * @param Board board
     * @return int score
     */
    @Override
    public int evaluate(Board board) {
        return score(board);
    }
}
