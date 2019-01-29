package edu.saddleback.tictactoe.board;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameBoardNode extends GridPane {


    public GameBoardNode(){

        GridPane boardGPane = new GridPane();
        boardGPane.setAlignment(Pos.CENTER);
        boardGPane.setHgap(5);
        boardGPane.setVgap(5);

        //Fills board with default blank spaces
        for(int i = 0; i < 3; i++){

            for( int j = 0; j < 3; j++){

                Pane tmpPane = new Pane();
                tmpPane.setMaxHeight(150);
                tmpPane.setMaxWidth(150);
                ImageView background = new ImageView(new Image("images/blank.png"));
                background.fitHeightProperty().bind(tmpPane.heightProperty());
                background.fitWidthProperty().bind(tmpPane.heightProperty());

                tmpPane.getChildren().add(background);
                //tmpPane.setOnMouseClicked(e->{

                //});
                boardGPane.add(tmpPane, i % 3, j % 3);

            }

        }


        this.getChildren().add(boardGPane);

    }

}
