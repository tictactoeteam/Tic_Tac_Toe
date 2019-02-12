package edu.saddleback.tictactoe.view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import edu.saddleback.tictactoe.controller.GameController;

public class LoginController {
    GameController controller;

    @FXML
    private Button singleplayerButton;
    @FXML
    private Button multiplayerButton;
    @FXML
    private Button playButton;

    @FXML
    private HBox playerNamesBox;
    @FXML
    private TextField player1NameField;
    @FXML
    private TextField player2NameField;

    public LoginController(GameController controller) {
        this.controller = controller;
//        VBox loginVBox = new VBox();
//        loginVBox.setAlignment(Pos.CENTER);
//        loginVBox.setSpacing(10);
//
//        ImageView titleImage = new ImageView(new Image("file:src/images/tictactoe2.png"));
//
//        HBox playerChoiceHBox = new HBox();
//        playerChoiceHBox.setSpacing(5);
//        playerChoiceHBox.setAlignment(Pos.CENTER);
//        Button singlePButton = new Button("Single Player");
//
//        Button multiPButton = new Button("Multiplayer");
//        playerChoiceHBox.getChildren().addAll(singlePButton, multiPButton);
//
//        HBox player1Field = new HBox();
//        player1Field.setSpacing(10);
//        player1Field.setAlignment(Pos.CENTER);
//        Text player1Text = new Text("Player One:");
//        TextField player1TextField = new TextField();
//        player1Field.getChildren().addAll(player1Text, player1TextField);
//
//        HBox player2Field = new HBox();
//        player2Field.setSpacing(10);
//        player2Field.setAlignment(Pos.CENTER);
//        Text player2Text = new Text("Player Two:");
//        TextField player2TextField = new TextField();
//        player2TextField.setVisible(false);
//        player2Text.setVisible(false);
//        player2Field.getChildren().addAll(player2Text, player2TextField);
//
//        singlePButton.setOnMouseClicked(e->{
//        player2TextField.setVisible(false);
//        player2Text.setVisible(false);
//        });
//
//        multiPButton.setOnMouseClicked(e->{
//            player2TextField.setVisible(true);
//            player2Text.setVisible(true);
//        });
//
//        loginVBox.getChildren().addAll(titleImage, playerChoiceHBox, player1Field, player2Field);
//
//        Button nextButton = new Button("Next");
//        nextButton.setOnMouseClicked(e->{
//
//            if(!player1TextField.getText().trim().isEmpty()){
//
//                VBox gameBoardVBox = new VBox();
//                gameBoardVBox.setSpacing(20);
//                gameBoardVBox.setAlignment(Pos.CENTER);
//
//                Text gameTitleText = new Text();
//                gameTitleText.setFont(new Font("Arial", 40));
//                gameTitleText.setStyle("-fx-font-weight: bold");
//                gameTitleText.setFill(Color.RED);
//                if(player2TextField.getText().trim().isEmpty())
//                    gameTitleText.setText(player1TextField.getText() + " vs. Mr. Bill AI");
//                else
//                    gameTitleText.setText(player1TextField.getText() + " vs. " + player2TextField.getText());
//
//
//                BoardView boardView = new BoardView(controller);
//                boardView.setAlignment(Pos.CENTER);
//                Text gameWinnerText = new Text();
//                gameWinnerText.setVisible(false);
//
//                gameBoardVBox.getChildren().addAll(gameTitleText, boardView, gameWinnerText);
//                Scene scene = new Scene(gameBoardVBox, 600, 600);
//
//                //Where are we putting the player names?
//                if(player2TextField.isVisible()){
//                    player2TextField.getText();
//                }
//                player1TextField.getText();
//
//                Stage appStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
//                appStage.setScene(scene);
//                appStage.show();
//
//            }else{
//
//                //GIVE ALERT MESSAGE TO LOGIN UI
//
//            }
//
//        });
//
//        nextButton.setAlignment(Pos.BOTTOM_RIGHT);
//
//        loginVBox.getChildren().add(nextButton);
    }

    protected void setMultiplayer(boolean multiplayer) {
        playerNamesBox.setVisible(multiplayer);
        // TODO handle logic in the GameController
    }

    protected void onSingleplayerClicked() {
        setMultiplayer(false);

    }

    protected void onMultiplayerClicked() {
        setMultiplayer(true);
    }

    protected void onNextClicked() {

    }
}
