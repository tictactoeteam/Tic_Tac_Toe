package edu.saddleback.tictactoe;

import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.view.SceneCoordinator;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    private static SceneCoordinator coordinator;

    public static GameController getController() {
        return coordinator.getController();
    }

    public static SceneCoordinator getCoordinator() {
        return coordinator;
    }

    @Override
    public void start(Stage window) throws Exception {
        window.setTitle("Tic Tac Toe");
        window.show();

        coordinator = new SceneCoordinator(window);

        if(coordinator.getController().gameStateExists())
            coordinator.showGameScene();
        else
            coordinator.showLoginScene();


    }

    @Override
    public void stop() {
        coordinator.onExitRequested();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
