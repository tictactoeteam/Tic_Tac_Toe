package edu.saddleback.tictactoe;

import edu.saddleback.tictactoe.view.BoardView;
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
    public void start(Stage primaryStage) {
        //Holds the entire scene, the game and the login screen
        StackPane gameStack = new StackPane();
        gameStack.setAlignment(Pos.CENTER);

        //Holds the user's name(s) on top, then the controller, then space to display the winner
        VBox gameUIVBox = new VBox();
        gameUIVBox.setAlignment(Pos.CENTER);

        //Shows the player(s) and who's turn it is.
        Text playerNamesText = new Text();


        //Holds the entire controller ui
        BoardView boardView = new BoardView();
        boardView.setAlignment(Pos.CENTER);


        //Shows the winner or tie when the game reaches end state.
        Text winnerText = new Text();
        winnerText.setVisible(false);

        gameUIVBox.getChildren().addAll(playerNamesText, boardView, winnerText);

        /***************************************************************************************************************
         * THIS IS WHERE THE LOGIN PAGE NODE WILL BE ADDED TO THE gameStack, ON TOP OF THE gameUIVBox NODE
         **************************************************************************************************************/

        gameStack.getChildren().add(gameUIVBox);
        //Sets up scene and stage
        Scene scene = new Scene(gameStack, 800, 600);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
