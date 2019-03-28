package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Launches the application, interacts with the scene controller to handle swapping scenes.
 */
public class TicTacToeApplication extends Application {
    private static SceneCoordinator coordinator;
    private static Stage Window;

    /**
     * Allows the game controller to be accessed by each scene.
     * @return
     */
    public static GameController getController(){return coordinator.getController();}

    /**
     * Allows the scene controller to be called from the scene views.
     * @return
     */
    public static SceneCoordinator getCoordinator(){return coordinator;}

    /**
     * Effectively resets the application
     */
    public static void newCoordinator(){

        coordinator = new SceneCoordinator(Window);

        Platform.runLater(() -> {
            try {
                coordinator.showLoginScene();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Establishes the window and allows the scene controller to take control of all scene manipulations.
     * @param window
     * @throws Exception
     */
    @Override
    public void start(Stage window) throws Exception {
        Window = window;
        Window.setTitle("Tic Tac Toe");
        Window.setWidth(600);
        Window.setHeight(475);
        Window.show();

        coordinator = new SceneCoordinator(Window);

        coordinator.showLoginScene();

    }

    /**
     * Allows for, on application termination, saving of all board and player information.
     */
    @Override
    public void stop(){coordinator.onExitRequested();}

}
