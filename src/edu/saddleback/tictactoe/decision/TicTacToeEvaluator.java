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

        if (sumX == 3)
            throw(new WinException(GamePiece.X));

        if (sumO == 2){
            if (sumX == 0)
                return -1;
            else
                return 0;
        }

        if (sumO == 3)
            throw(new WinException(GamePiece.X));

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

        if (sumX == 3)
            throw(new WinException(GamePiece.X));

        if (sumO == 2){
            if (sumX == 0)
                return -1;
            else
                return 0;
        }

        if (sumO == 3)
            throw(new WinException(GamePiece.O));

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

        if (sumX == 3)
            throw(new WinException(GamePiece.X));

        if (sumO == 2){
            if (sumX == 0)
                numStreaks -= 1;
        }

        if (sumO == 3)
            throw(new WinException(GamePiece.O));


        if (a[2][0] ==  a[0][2]){
            if (a[1][1] ==  a[2][0] && a[1][1] != GamePiece.EmptySpace)
                throw(new WinException(a[1][1]));

            if (a[2][0] == GamePiece.O)
                numStreaks -=1;

            if(a[2][0] == GamePiece.X)
                numStreaks +=1;

        }



        return 0;
    }

    // Basic evaluator. For a winning position grants 100 points, for a double streak 10 points, and for a single streak 1 point.
    // Positive points are granted to X-player and negative to O-player
    @Override
    public int evaluate(Board b) {
        final GamePiece[][] array = b.getBoard();

        int sumStreaks = 0;

        try {
            for (int i = 0; i < 3; ++i)
                sumStreaks += calcRowStreaks(i, array);

            for (int i = 0; i < 3; ++i)
                sumStreaks += calcColumnStreaks(i, array);

            sumStreaks += calcDiagonalStreaks(array);


            

        }
        catch(WinException e){
            if(e.player == GamePiece.X)
                return 100;
            else
                return -100;
        }

    }
}
