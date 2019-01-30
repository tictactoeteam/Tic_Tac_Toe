package edu.saddleback.tictactoe.board;

import java.io.Serializable;
import edu.saddleback.tictactoe.decision.StaticEvaluator;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

/**
 * This class represents a controller for all data and ui functions for the game board grid pane.
 */
public class BoardController implements Serializable {

    /**
     * Describes the chosen state of the grid box, either 'X', 'O', or empty space.
     */
    public enum Piece{
        X, O, EmptySpace
    }

/***********************************************************************************************************************
*******PIECE[][] WILL BE THE MAIN PART OF THE DATA THAT NEEDS TO BE STORED IN A BINARY FILE AT SOME POINT***************
 ************************************CLEAR THIS AFTER IMPLEMENTATION***************************************************/
    private Piece[][] boardItself;

    //IF this is even, then it is player 1's turn, else player 2/computer's turn
    public static int whosTurn;

    /**
     * Constructor
     * Initializes the board data to the EmptySpace enum.
     */
    public BoardController(){

        whosTurn = 0;
        boardItself = new Piece[3][3];

        for (int i=0; i<3; ++i){
            for (int j=0; j<3; ++j){
                boardItself[i][j] = Piece.EmptySpace;
            }
        }
    }

    /**
     * Checks if the selected grid box has been chosen, if so a GridAlreadyChosenException is thrown, else the Piece[][]
     * is updated with the correct image enum.
     * @param piece
     * @param row
     * @param column
     * @throws GridAlreadyChosenException
     */
    private void PlacePiece(Piece piece, int row, int column) throws GridAlreadyChosenException{

        if (boardItself[row][column] == Piece.EmptySpace){

            boardItself[row][column] = piece;
            whosTurn++;

        }else
            throw new GridAlreadyChosenException("Grid Piece [" + row + "] [" + column + "] has already been chosen");

    }

    /**
     * Updates the Piece[][] with the current move.
     * @param gridBox
     */
    public void gridBoxClicked(GridBox gridBox){

        Piece tmpPiece;
        switch (whosTurn % 2){

            case 0: tmpPiece = Piece.X;
                break;
            default: tmpPiece = Piece.O;

        }

        //Attempts to update the Piece[][] with the ne choice.
        try{

            PlacePiece(tmpPiece, gridBox.getGridRowIndex(), gridBox.getGridColumnIndex());

        }catch(GridAlreadyChosenException ex){System.out.println("***Error" + ex + "***");}

    }

    //Getter
    public Piece getPiece(int row, int column){return boardItself[row][column];}

    /**
     * Updates the board's ui to show the current player(s) move(s) based on the Piece[][].
     */
    public void updateUI(GridBox gridBox, GridPane boardGPane){

        String path;
        int gridBoxRowIndex = gridBox.getGridRowIndex();
        int gridBoxColumnIndex = gridBox.getGridColumnIndex();

        if(getPiece(gridBoxRowIndex, gridBoxColumnIndex) == Piece.X)
            path = "images/x.png";
        else if(getPiece(gridBoxRowIndex, gridBoxColumnIndex) == Piece.O)
            path = "images/o.png";
        else
            path = "images/blank.png";

        gridBox.getBackgroundImageView().setImage(new Image(path));

    }

    /**
     * Custom exception that is triggered if either player tries to click a grid box that has already been chosen.
     */
    private class GridAlreadyChosenException extends Exception {

        public GridAlreadyChosenException(String errorMessage){super(errorMessage);}

    }

}
