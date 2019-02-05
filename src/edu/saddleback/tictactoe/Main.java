package edu.saddleback.tictactoe;

import javafx.application.Application;
import edu.saddleback.tictactoe.view.BoardView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
        StackPane loginStack = new StackPane();
        String playerOne;
        String playerTwo;

        gameStack.setAlignment(Pos.CENTER);

        VBox loginBox = new VBox(20);

        Image image = new Image("file:src/images/tictactoe2.png");
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(600);
        imageView.setFitHeight(200);

        Button singlePlayer = new Button("SinglePlayer");
        Button multiplayer = new Button("Multiplayer");
        Button next = new Button("Next");
        Label playerOneLabel = new Label("Player One: ");
        Label playerTwoLabel = new Label("Player Two: ");

        TextField firstPlayer = new TextField();
        TextField secondPlayer = new TextField();

        firstPlayer.setMaxWidth(100);
        secondPlayer.setMaxWidth(100);

        firstPlayer.setVisible(false);
        secondPlayer.setVisible(false);

        //Lambda expressions to toggle whether one player textfield is visible or both on button click
        singlePlayer.setOnAction(e -> {
            firstPlayer.setVisible(true);
            secondPlayer.setVisible(false);
        });

        multiplayer.setOnAction(e -> {
            secondPlayer.setVisible(true);
            firstPlayer.setVisible(true);
        });

        loginBox.setAlignment(Pos.CENTER);
        loginBox.getChildren().addAll(imageView, singlePlayer, multiplayer, firstPlayer, secondPlayer, next);

        loginStack.getChildren().add(loginBox);

        Scene loginScene = new Scene(loginStack, 600, 600);

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
        primaryStage.setScene(loginScene);
        primaryStage.show();

        //This has to come after we initialize the second scene. Lambda expression to change scenes
        playerOne = firstPlayer.getText();
        playerTwo = secondPlayer.getText();
        next.setOnAction(e -> primaryStage.setScene(scene));

    }
}

