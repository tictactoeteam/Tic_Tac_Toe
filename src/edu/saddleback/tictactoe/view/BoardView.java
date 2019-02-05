package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 * This class represents the actual tic tac toe controller being displayed in the center of the edu.saddleback.tictactoe.Main UI. It holds the
 * controller class that handles all game data, ui controls, and data read and write capabilities.
 */
public class BoardView extends GridPane {
    private GameController controller;
    static int gridBoxIndex;

    /**
     * Constructor
     * Populates the grid pane controller with the default spaces, and initializes all data for a new game.
     */
    public BoardView() {
        //Initializes static integer for gridBox indices and the controller controller.
        gridBoxIndex = 0;
        controller = new GameController();

        GridPane boardGPane = new GridPane();
        boardGPane.setAlignment(Pos.CENTER);
        boardGPane.setHgap(5);
        boardGPane.setVgap(5);

        //Fills controller with default blank images
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                GridBox gridBox = new GridBox(gridBoxIndex++);

                //Handles player 1 or player 2/computer making a choice
                gridBox.setOnMouseClicked(e->{
                    GridBox tmpGB = (GridBox)e.getSource();
                    controller.onGridClicked(tmpGB);
                    controller.updateUi(gridBox);
                });

                boardGPane.add(gridBox, j, i);
            }
        }

        this.getChildren().add(boardGPane);
    }
}