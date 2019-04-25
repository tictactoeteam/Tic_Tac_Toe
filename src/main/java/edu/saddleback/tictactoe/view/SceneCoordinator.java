package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Handles how the scenes switch around the main window.
 */
public class SceneCoordinator {

    private GameController controller;
    private Stage window;

    /**
     * Initializes the game controller into the scene coordinator for use throughout the application.
     * @param window
     */
    public SceneCoordinator(Stage window){

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
    public void showLoginScene() throws IOException {
        URL url = new File("src/main/res/layout/login.fxml").toURL();
        Parent layout = FXMLLoader.load(url);
        this.window.setScene(new Scene(layout));

    }

    /**
     * Fetches the lobby scene.
     * @throws Exception
     */
    public void showLobbyScene() throws Exception{

        URL url = new File("src/main/res/layout/lobby.fxml").toURL();
        Parent layout = FXMLLoader.load(url);
        this.window.setScene(new Scene(layout));

    }

    /**
     * Fetches the game scene.
     * @throws Exception
     */
    public void showGameScene() throws Exception{

        URL url = new File("src/main/res/layout/game.fxml").toURL();
        Parent layout = FXMLLoader.load(url);
        this.window.setScene(new Scene(layout));

    }

    /**
     * Fetches the history scene.
     * @throws Exception
     */
    public void showHistoryScene() throws Exception{

        URL url = new File("src/main/res/layout/history.fxml").toURL();
        Parent layout = FXMLLoader.load(url);
        this.window.setScene(new Scene(layout));

    }

    /**
     * Fetches the Winner scene.
     * @throws Exception
     */
    public void showWinnerScene() throws Exception{

        URL url = new File("src/main/res/layout/winner.fxml").toURL();
        Parent layout = FXMLLoader.load(url);
        this.window.setScene(new Scene(layout));

    }

    /**
     * Calls the game to be saved on an application termination.
     */
    public void onExitRequested(){this.controller.onExitRequested();}

}
