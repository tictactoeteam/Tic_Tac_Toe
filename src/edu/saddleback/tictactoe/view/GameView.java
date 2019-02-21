package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.MainApplication;
import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GameView {
    private GameController controller;

    @FXML
    HBox radioHBoxDiff;

    @FXML
    Text playerNames;

    @FXML
    Button resetButton;

    public void handleEasy(){ controller.setDifficulty("Easy Mode"); }

    public void handleBillMode(){ controller.setDifficulty("Mr. Bill Mode"); }

    public void onResetClicked(){

        controller.deleteSaveFile();
        System.exit(0);

    }

    @FXML
    protected void initialize() {
        controller = MainApplication.getController();
        playerNames.setText(controller.getPlayer1Name() + " vs. " + controller.getPlayer2Name());
        if(controller.isMultiplayer()){ radioHBoxDiff.setVisible(false); }
    }
}
