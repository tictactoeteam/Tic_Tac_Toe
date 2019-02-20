package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.MainApplication;
import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GameView{

    GameController controller;
    @FXML
    private Text playerNames;
    @FXML
    BoardView boardView;
    @FXML
    Button resetButton;

    /**
     * Deletes the same file and closes the application.
     */
    public void onResetClicked(){

        controller.deleteSaveFile();
        System.exit(0);

    }

    /**
     * Initializes the game ui with the entered names and moves the controller to the gameview.
     */
    @FXML
    protected void initialize() {
        controller = MainApplication.getController();
        playerNames.setText(controller.getPlayer1Name() + " vs. " + controller.getPlayer2Name());
    }
}
