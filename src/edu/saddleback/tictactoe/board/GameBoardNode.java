package edu.saddleback.tictactoe.board;

import javafx.scene.layout.GridPane;

public class GameBoardNode extends GridPane {

    public GameBoardNode(){

        GridPane boardGPane = new GridPane();



        this.getChildren().add(boardGPane);

    }

}
