
package edu.saddleback.tictactoe;


import java.io.Serializable;
import edu.saddleback.tictactoe.decision.StaticEvaluator;





public class Board implements Serializable {
    public enum Piece{
        X, O, EmptySpace
    }


    private Piece[][] boardItself;


    public Board(){
        boardItself = new Piece[3][3];

        for (int i=0; i<3; ++i){
            for (int j=0; j<3; ++j){
                boardItself[i][j] = Piece.EmptySpace;
            }
        }
    }


    public boolean PlacePiece(Piece piece, int row, int column){
        if (boardItself[row][column] == Piece.EmptySpace){
            boardItself[row][column] = piece;
            return true;
        }
        else{
            return false;
        }
    }

    public Piece getPiece(int row, int column){
        return boardItself[row][column];
    }


}
