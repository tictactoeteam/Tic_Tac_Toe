package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;
import edu.saddleback.tictactoe.view.GridBox;
import javafx.scene.image.Image;

/**
 * This class represents a controller for all data and ui functions for the game controller grid pane.
 */
public class GameController {
    private Board board;

    public GameController(){
        this.board = new Board();
    }

    /**
     * Updates the Piece[][] with the current move.
     * @param gridBox
     */
    public void onGridClicked(GridBox gridBox) {
        try {
            GamePiece piece = board.isXTurn() ? GamePiece.X : GamePiece.O;
            board.set(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);
        } catch (GridAlreadyChosenException e) {
            // TODO: re-prompt for another position
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the controller's ui to show the current player(s) move(s) based on the Piece[][].
     */
    public void updateUi(GridBox gridBox){
        String path;
        int gridBoxRowIndex = gridBox.getGridRowIndex();
        int gridBoxColumnIndex = gridBox.getGridColumnIndex();

        if(board.get(gridBoxRowIndex, gridBoxColumnIndex) == GamePiece.X)
            path = "file:src/images/x.png";
        else if(board.get(gridBoxRowIndex, gridBoxColumnIndex) == GamePiece.O)
            path = "file:src/images/o.png";
        else
            path = "file:src/images/blank.png";

        gridBox.getBackgroundImageView().setImage(new Image(path));
    }
}
