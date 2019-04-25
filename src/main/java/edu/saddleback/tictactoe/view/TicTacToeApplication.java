package edu.saddleback.tictactoe.view;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import edu.saddleback.tictactoe.controller.GameController;

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
     * Allows for the window sizes to be changes.
     * @param width
     * @param height
     */
    public static void setWindowSize(double width, double height){

        Window.setWidth(width);
        Window.setHeight(height);
    }

    /**
     * Establishes the window and allows the scene controller to take control of all scene manipulations.
     * @param window
     * @throws Exception
     */
    @Override
    public void start(Stage window) throws IOException {
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
