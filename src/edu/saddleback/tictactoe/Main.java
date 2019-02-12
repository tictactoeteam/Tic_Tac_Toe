package edu.saddleback.tictactoe;

import edu.saddleback.tictactoe.view.BoardView;
import edu.saddleback.tictactoe.view.LoginScene;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Launches and initializes the game controller, as well as the login page
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create the Scene
        Scene scene = new Scene(new LoginScene(), 800, 600);

        primaryStage.setScene(scene);

        primaryStage.setTitle("Tic-Tac-Toe");

        primaryStage.show();
    }
}

