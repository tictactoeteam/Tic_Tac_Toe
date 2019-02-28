package edu.saddleback.tictactoe;

import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.view.SceneCoordinator;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launches the application, interacts with the scene controller to handle swapping scenes.
 */
public class MainApplication extends Application {
    private static SceneCoordinator coordinator;

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
     * Establishes the window and allows the scene controller to take control of all scene manipulations.
     * @param window
     * @throws Exception
     */
    @Override
    public void start(Stage window) throws Exception {
        window.setTitle("Tic Tac Toe");
        window.setWidth(600);
        window.setHeight(450);
        window.show();

        coordinator = new SceneCoordinator(window);

        if(coordinator.getController().gameStateExists())
            coordinator.showGameScene();
        else
            coordinator.showLoginScene();

    }

    /**
     * Allows for, on application termination, saving of all board and player information.
     */
    @Override
    public void stop(){coordinator.onExitRequested();}

    /**
     * Launches the application.
     * @param args
     */
    public static void main(String[] args){launch(args);}

}
