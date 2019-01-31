package edu.saddleback.tictactoe.board;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * This class represents a single grid box on the tic tac toe board.
 */
public class GridBox extends Pane {

    private int gridIndex;                 //Keeps track of which GridBox is clicked by the user for their move.
    private ImageView background;          //Holds the image of either the 'x', 'o', or default background.
    private final int GRID_BOX_SIZE = 150; //The pixel size of the GridBox object size in the tic tac toe board.

    public GridBox(int index){

        //Assigns grid box index
        gridIndex = index;

        //Sets up grid box and its image
        setMaxHeight(GRID_BOX_SIZE);
        setMaxWidth(GRID_BOX_SIZE);
        background = new ImageView(new Image("file:src/images/blank.png"));
        background.fitHeightProperty().bind(heightProperty());
        background.fitWidthProperty().bind(heightProperty());

        this.getChildren().add(background);

    }

    //Getters
    public int getGridIndex(){return gridIndex;}
    public ImageView getBackgroundImageView(){return background;}
    public int getGridColumnIndex(){

        if(gridIndex == 0 || gridIndex == 3 || gridIndex == 6)
            return 0;
        else if(gridIndex == 1 || gridIndex == 4 || gridIndex == 7)
            return 1;
        else
            return 2;

    }
    public int getGridRowIndex(){

        if(gridIndex < 3)
            return 0;
        else if(gridIndex < 6)
            return 1;
        else
            return 2;

    }


    /**
     * Changes the gridBox's image displayed.
     * @param path
     */
    public void setGridImage(String path){

        background = null;
        background = new ImageView(new Image(path));

    }

}
