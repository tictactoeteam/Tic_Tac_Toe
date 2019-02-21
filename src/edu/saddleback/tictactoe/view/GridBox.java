package edu.saddleback.tictactoe.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * This class represents a single grid box on the tic tac toe controller.
 */
public class GridBox extends Pane {

    private static final int SIZE_PX = 150; //The pixel size of the GridBox object size in the tic tac toe controller.
    private int gridIndex;                 //Keeps track of which GridBox is clicked by the user for their move.
    private ImageView background;          //Holds the image of either the 'x', 'o', or default background.

    /**
     * Constructs the grid box object.
     * @param index
     */
    public GridBox(int index){
        //Assigns grid box index
        gridIndex = index;

        //Sets up grid box and its image
        setMaxHeight(GridBox.SIZE_PX);
        setMaxWidth(GridBox.SIZE_PX);
        background = new ImageView(new Image("file:src/images/blank.png"));
        background.fitHeightProperty().bind(heightProperty());
        background.fitWidthProperty().bind(heightProperty());

        this.getChildren().add(background);

    }

    /**
     *
     * @return the background image view.
     */
    public ImageView getBackgroundImageView(){ return background; }

    /**
     *
     * @return the grid column index.
     */
    public int getGridColumnIndex(){return gridIndex % 3;}

    /**
     *
     * @return the grid row index.
     */
    public int getGridRowIndex(){return gridIndex / 3; }

    /**
     * Changes the gridBox's image displayed.
     * @param path
     */
    public void setGridImage(String path){
        background = new ImageView(new Image(path));
    }
}
