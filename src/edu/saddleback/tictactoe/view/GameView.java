package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.MainApplication;
import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GameView {
    private GameController controller;

    @FXML
    Text playerNames;

    @FXML
    BoardView boardView;

    @FXML
    protected void initialize() {
        controller = MainApplication.getController();
        playerNames.setText(controller.getPlayer1Name() + " vs. " + controller.getPlayer2Name());
    }
}
