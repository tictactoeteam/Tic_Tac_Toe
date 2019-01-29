package edu.saddleback.tictactoe.board;

import javafx.geometry.Pos;
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
                Rectangle tmpRec = new Rectangle();
                tmpRec.setHeight(150);
                tmpRec.setWidth(150);
                tmpRec.setFill(Color.GRAY);
                tmpPane.getChildren().add(tmpRec);
                boardGPane.add(tmpRec, i % 3, j % 3);

            }

        }


        this.getChildren().add(boardGPane);

    }

}
