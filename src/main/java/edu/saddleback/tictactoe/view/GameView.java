package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * This class handles all the UI controls for the game UI, including setting the game AI difficulty and reseting the
 * game. Handles all UI data.
 */
public class GameView{

    GameController controller;
    @FXML
    HBox radioHBoxDiff;
    @FXML
    Text player1Name;
    @FXML
    Text player2Name;
    @FXML
    Button resetButton;

    /**
     * Deletes the same file and closes the application.
     */
    public void onResetClicked(){
        controller.resetGame();
    }

    /**
     * Initializes the game ui with the entered names and moves the controller to the gameview.
     */
    @FXML
    protected void initialize(){
        TicTacToeApplication.setWindowSize(600, 475);
        controller = TicTacToeApplication.getController();
        player1Name.setText(controller.getPlayer1Name());
        player2Name.setText(controller.getPlayer2Name());

    }


}