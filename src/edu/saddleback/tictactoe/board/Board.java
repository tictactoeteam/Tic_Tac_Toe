package edu.saddleback.tictactoe.board;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

/**
 * This class represents a controller for all data and ui functions for the game board grid pane.
 */
public class Board implements Serializable {
/***********************************************************************************************************************
*******PIECE[][] WILL BE THE MAIN PART OF THE DATA THAT NEEDS TO BE STORED IN A BINARY FILE AT SOME POINT***************
 ************************************CLEAR THIS AFTER IMPLEMENTATION***************************************************/
    private GamePiece[][] pieces;

    //IF this is even, then it is player 1's turn, else player 2/computer's turn
    public static int roundNumber;

    /**
     * Constructor
     * Initializes the board data to the EmptySpace enum.
     */
    public Board(){
        roundNumber = 0;
        pieces = new GamePiece[3][3];

        for (int i=0; i<3; ++i){
            for (int j=0; j<3; ++j){
                pieces[i][j] = null;
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
    private void placePiece(GamePiece piece, int row, int column) throws GridAlreadyChosenException{
        if (pieces[row][column] == null){
            pieces[row][column] = piece;
            roundNumber++;
        } else {
            throw new GridAlreadyChosenException(row, column, "Grid Piece [" + row + "] [" + column + "] has already been chosen");
        }

    }

    /**
     * Updates the Piece[][] with the current move.
     * @param gridBox
     */
    public void gridBoxClicked(GridBox gridBox){
        GamePiece tmpPiece = roundNumber % 2 == 0 ? GamePiece.X : GamePiece.O;

        //Attempts to update the Piece[][] with the choice.
        try{
            placePiece(tmpPiece, gridBox.getGridRowIndex(), gridBox.getGridColumnIndex());
        } catch(GridAlreadyChosenException ex) {
            // TODO: do not fail here, have the player choose a different position
        }
    }

    //Getter
    public GamePiece getPiece(int row, int column){
        return pieces[row][column];
    }

    /**
     * Updates the board's ui to show the current player(s) move(s) based on the Piece[][].
     */
    public void updateUi(GridBox gridBox, GridPane boardGPane){
        String path;
        int gridBoxRowIndex = gridBox.getGridRowIndex();
        int gridBoxColumnIndex = gridBox.getGridColumnIndex();

        if(getPiece(gridBoxRowIndex, gridBoxColumnIndex) == GamePiece.X)
            path = "file:src/images/x.png";
        else if(getPiece(gridBoxRowIndex, gridBoxColumnIndex) == GamePiece.O)
            path = "file:src/images/o.png";
        else
            path = "file:src/images/blank.png";

        gridBox.getBackgroundImageView().setImage(new Image(path));
    }
}
