package edu.saddleback.tictactoe.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginScene extends Pane {

    public LoginScene(){

        VBox loginVBox = new VBox();
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(10);

        ImageView titleImage = new ImageView(new Image("file:src/images/tictactoe2.png"));

        HBox playerChoiceHBox = new HBox();
        playerChoiceHBox.setSpacing(5);
        playerChoiceHBox.setAlignment(Pos.CENTER);
        Button singlePButton = new Button("Single Player");

        Button multiPButton = new Button("Multiplayer");
        playerChoiceHBox.getChildren().addAll(singlePButton, multiPButton);

        HBox player1Field = new HBox();
        player1Field.setSpacing(10);
        player1Field.setAlignment(Pos.CENTER);
        Text player1Text = new Text("Player One:");
        TextField player1TextField = new TextField();
        player1Field.getChildren().addAll(player1Text, player1TextField);

        HBox player2Field = new HBox();
        player2Field.setSpacing(10);
        player2Field.setAlignment(Pos.CENTER);
        Text player2Text = new Text("Player Two:");
        TextField player2TextField = new TextField();
        player2TextField.setVisible(false);
        player2Text.setVisible(false);
        player2Field.getChildren().addAll(player2Text, player2TextField);

        singlePButton.setOnMouseClicked(e->{
        player2TextField.setVisible(false);
        player2Text.setVisible(false);
        });

        multiPButton.setOnMouseClicked(e->{
            player2TextField.setVisible(true);
            player2Text.setVisible(true);
        });

        loginVBox.getChildren().addAll(titleImage, playerChoiceHBox, player1Field, player2Field);

        Button nextButton = new Button("Next");
        nextButton.setOnMouseClicked(e->{
            BoardView boardView = new BoardView();
            boardView.setAlignment(Pos.CENTER);
            Scene scene = new Scene(boardView, 600, 600);

            //Where are we putting the player names?
            if(player2TextField.isVisible()){
                player2TextField.getText();
            }
            player1TextField.getText();

            Stage appStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();
        });

        nextButton.setAlignment(Pos.BOTTOM_RIGHT);

        loginVBox.getChildren().add(nextButton);

        this.getChildren().add(loginVBox);
    }

}
