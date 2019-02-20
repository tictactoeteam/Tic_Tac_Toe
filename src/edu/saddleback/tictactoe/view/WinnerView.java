package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.MainApplication;
import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class WinnerView {
    private GameController controller;

    @FXML
    Text winnerText;

    @FXML
    protected void initialize() {
        controller = MainApplication.getController();
        winnerText.setText(controller.generateWinMessage(controller.checkWinner()));
    }

}
