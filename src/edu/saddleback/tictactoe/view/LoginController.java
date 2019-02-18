package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import edu.saddleback.tictactoe.controller.GameController;

public class LoginController {
    private GameController controller;

    @FXML
    private HBox playerNamesBox;

    public void initialize() {
        controller = MainApplication.getController();
    }

    public void setMultiplayer(boolean multiplayer) {
        playerNamesBox.setVisible(multiplayer);
        // TODO handle logic in the GameController
    }

    public void onSingleplayerClicked() {
        setMultiplayer(false);
    }

    public void onMultiplayerClicked() {
        setMultiplayer(true);
    }

    public void onPlayClicked() {
        MainApplication.getCoordinator().showGameScene();
    }
}
