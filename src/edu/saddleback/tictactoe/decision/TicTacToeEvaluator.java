package edu.saddleback.tictactoe.decision;

import edu.saddleback.tictactoe.board.Board;
import edu.saddleback.tictactoe.board.GamePiece;

public class TicTacToeEvaluator implements StaticEvaluator {


    private int calcRowStreaks(int r, GamePiece[][] a){


        int sumX = 0;
        int sumO = 0;
        for (int i=0; i<3; ++i){
            if (a[r][i] == GamePiece.X)
                sumX++;

            if (a[r][i] == GamePiece.O)
                sumO++;
        }

        if (sumX == 2){
            if(sumO == 0)
                return 1;
            else
                return 0;
        }



        if (sumO == 2){
            if (sumX == 0)
                return -1;
            else
                return 0;
        }


        return 0;

    }


    private int calcColumnStreaks(int c, GamePiece[][] a){

        int sumX = 0;
        int sumO = 0;
        for (int i=0; i<3; ++i){
            if (a[i][c] == GamePiece.X)
                sumX++;

            if (a[i][c] == GamePiece.O)
                sumO++;
        }

        if (sumX == 2){
            if(sumO == 0)
                return 1;
            else
                return 0;
        }


        if (sumO == 2){
            if (sumX == 0)
                return -1;
            else
                return 0;
        }


        return 0;

    }


    private int calcDiagonalStreaks(GamePiece[][] a){

        int sumX = 0;
        int sumO = 0;
        for (int i=0; i<3; ++i){
            if (a[i][i] == GamePiece.X)
                sumX++;

            if (a[i][i] == GamePiece.O)
                sumO++;
        }


        int numStreaks = 0;

        if (sumX == 2){
            if(sumO == 0)
                numStreaks += 1;
        }


        if (sumO == 2){
            if (sumX == 0)
                numStreaks -= 1;
        }







        if (a[2][0] ==  a[0][2]){

            if (a[1][1] == null) {
                if (a[2][0] == GamePiece.O)
                    numStreaks -= 1;

                if (a[2][0] == GamePiece.X)
                    numStreaks += 1;

            }


        }



        return numStreaks;
    }

    private GamePiece weGotTheWinner(GamePiece[][] a){

        return null;
    }


    // Just counts the number of streaks, returns negative for O-player and positive for X-player
    @Override
    public int evaluate(Board b) {
        final GamePiece[][] array = b.getBoardCopy();

        int sumStreaks = 0;


        if (weGotTheWinner(array) == GamePiece.X){
            return 100;
        }
        else if(weGotTheWinner(array) == GamePiece.O){
            return -100;
        }


        for (int i = 0; i < 3; ++i)
            sumStreaks += calcRowStreaks(i, array);
        for (int i = 0; i < 3; ++i)
            sumStreaks += calcColumnStreaks(i, array);

        sumStreaks += calcDiagonalStreaks(array);


        return sumStreaks;
            



    }
}
