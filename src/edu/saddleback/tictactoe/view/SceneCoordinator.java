package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneCoordinator {

    private GameController controller;
    private Stage window;

    /**
     * Initializes the game controller into the scene coordinator for use throughout the application.
     * @param window
     */
    public SceneCoordinator(Stage window) {
        this.window = window;
        this.controller = new GameController();
    }

    /**
     *
     * @return the game controller.
     */
    public GameController getController() {
        return controller;
    }

    /**
     * Fetches the login scene.
     * @throws Exception
     */
    public void showLoginScene() throws Exception {
        Parent layout = FXMLLoader.load(getClass().getResource("/res/layout/login.fxml"));
        this.window.setScene(new Scene(layout));
    }

    /**
     * Fetches the game scene.
     * @throws Exception
     */
    public void showGameScene() throws Exception {
        Parent layout = FXMLLoader.load(getClass().getResource("/res/layout/game.fxml"));
        this.window.setScene(new Scene(layout));
    }

    /**
     * Calls the game to be saved on an application termination.
     */
    public void onExitRequested() {
        this.controller.onExitRequested();
    }
}
