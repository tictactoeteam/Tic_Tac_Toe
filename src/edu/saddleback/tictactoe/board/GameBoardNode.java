package edu.saddleback.tictactoe.board;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * This class represents the actual tic tac toe board being displayed in the center of the Main UI. It holds the
 * controller class that handles all game data, ui controls, and data read and write capabilities.
 */
public class GameBoardNode extends GridPane {

    private BoardController boardController;
    static int gridBoxIndex;

    /**
     * Constructor
     * Populates the grid pane board with the default spaces, and initializes all data for a new game.
     */
    public GameBoardNode(){

        //Initializes static integer for gridBox indices and the board controller.
        gridBoxIndex = 0;
        boardController = new BoardController();

        GridPane boardGPane = new GridPane();
        boardGPane.setAlignment(Pos.CENTER);
        boardGPane.setHgap(5);
        boardGPane.setVgap(5);

        //Fills board with default blank images
        for(int i = 0; i < 3; i++){

            for(int j = 0; j < 3; j++){

                GridBox gridBox = new GridBox(gridBoxIndex++);

                //Handles player 1 or player 2/computer making a choice
                gridBox.setOnMouseClicked(e->{

                    GridBox tmpGB = (GridBox)e.getSource();
                    boardController.gridBoxClicked(tmpGB);
                    boardController.updateUI(tmpGB, boardGPane);

                });

                boardGPane.add(gridBox, j, i);

            }

        }

        this.getChildren().add(boardGPane);

    }

}