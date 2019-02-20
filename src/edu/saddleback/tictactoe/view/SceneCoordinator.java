package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneCoordinator {
    private GameController controller;

    private Stage window;

    public SceneCoordinator(Stage window) {
        this.window = window;
        this.controller = new GameController();
    }

    public GameController getController() {
        return controller;
    }

    public void showLoginScene() throws Exception {
        Parent layout = FXMLLoader.load(getClass().getResource("/res/layout/login.fxml"));
        this.window.setScene(new Scene(layout));
    }

    public void showGameScene() throws Exception {
        Parent layout = FXMLLoader.load(getClass().getResource("/res/layout/game.fxml"));
        this.window.setScene(new Scene(layout));
    }

    public void showWinnerScene() throws Exception {
        Parent layout = FXMLLoader.load(getClass().getResource("/res/layout/winner.fxml"));
        this.window.setScene(new Scene(layout));
    }

    public void onExitRequested() {
        this.controller.onExitRequested();
    }
}
