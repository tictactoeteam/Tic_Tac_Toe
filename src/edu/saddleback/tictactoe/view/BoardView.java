package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.MainApplication;
import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

/**
 * This class represents the actual tic tac toe controller being displayed in the center of the Main UI. It holds the
 * controller class that handles all game data, ui controls, and data read and write capabilities.
 */
public class BoardView extends GridPane {
    private GameController controller;

    private GridBox[][] grid;
    static int gridBoxIndex;

    /**
     * Constructor
     * Populates the grid pane controller with the default spaces, and initializes all data for a new game.
     */
    public BoardView() {
        this.controller = MainApplication.getController();
        //Initializes static integer for gridBox indices and the controller controller.
        gridBoxIndex = 0;
        this.grid = new GridBox[3][3];

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
                    try {
                        controller.onGridClicked(tmpGB);
                    }
                    catch(Exception ex){
                        System.out.println("This shouldn't happen, rethink your life");
                    }
                });

                boardGPane.add(gridBox, j, i);
                this.grid[i][j] = gridBox; //todo what
            }
        }

        this.getChildren().add(boardGPane);
        controller.addBoardListener(this::onUpdate);
    }

    public void onUpdate(Board board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String path;


                if(board.get(i, j) == GamePiece.X)
                    path = getClass().getResource("/res/images/x.png").toString();
                else if(board.get(i, j) == GamePiece.O)
                    path = getClass().getResource("/res/images/o.png").toString();
                else
                    path = getClass().getResource("/res/images/blank.png").toString();

                grid[i][j].getBackgroundImageView().setImage(new Image(path));
            }
        }

    }
}