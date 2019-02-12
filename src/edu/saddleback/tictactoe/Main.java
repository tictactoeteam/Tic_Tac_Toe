package edu.saddleback.tictactoe;

import edu.saddleback.tictactoe.view.MainApplication;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launches and initializes the game controller, as well as the login page
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MainApplication.start(stage);
    }

    @Override
    public void stop() {
        MainApplication.stop();
    }
}

