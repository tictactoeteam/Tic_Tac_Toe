package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.scene.text.Text;

public class LoginView {
    private GameController controller;

    @FXML
    private HBox playerNamesBox;

    @FXML
    private HBox singlePlayerNameBox;

    @FXML
    private TextField playerName;

    @FXML
    private TextField player1Name;

    @FXML
    private TextField player2Name;

    public void initialize() {
        controller = MainApplication.getController();
    }

    public void setMultiplayer(boolean multiplayer) {
        playerNamesBox.setVisible(multiplayer);
        singlePlayerNameBox.setVisible(!multiplayer);

        controller.setMultiplayer(true);
    }

    public void onSingleplayerClicked() {
        setMultiplayer(false);
    }

    public void onMultiplayerClicked() {
        setMultiplayer(true);
    }

    public void onPlayClicked() throws Exception {
        if (controller.isMultiplayer()) {
            controller.setPlayer1Name(playerName.getText());
            controller.setPlayer2Name("Mr. Bill");
        } else {
            controller.setPlayer1Name(player1Name.getText());
            controller.setPlayer2Name(player2Name.getText());
        }

        MainApplication.getCoordinator().showGameScene();
    }
}
