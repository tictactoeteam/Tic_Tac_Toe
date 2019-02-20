package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.MainApplication;
import edu.saddleback.tictactoe.decision.Node;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import edu.saddleback.tictactoe.controller.GameController;
import javafx.scene.text.Text;

public class LoginView {
    private GameController controller;

    private boolean mrBillGoesFirst;

    @FXML
    private HBox radioHbox;
    @FXML
    private RadioButton radioPlayer;

    @FXML
    private RadioButton radioBill;

    @FXML
    private HBox playerNamesBox;

    @FXML
    private HBox singlePlayerNameBox;

    @FXML
    private TextField playerName;

    @FXML
    private TextField player1Name;

    @FXML
    private TextField player2Name;

    @FXML
    private ComboBox difficultyCombo;

    public void initialize() {
        controller = MainApplication.getController();
    }

    public void setMultiplayer(boolean multiplayer) {
        playerNamesBox.setVisible(multiplayer);
        singlePlayerNameBox.setVisible(!multiplayer);
        difficultyCombo.setVisible(!multiplayer);

        controller.setMultiplayer(multiplayer);
    }

    public void onSingleplayerClicked() {
        radioHbox.setVisible(true);
        setMultiplayer(false);
    }

    public void onMultiplayerClicked() {
        radioHbox.setVisible(false);
        setMultiplayer(true);
    }

    public void handlePlayer(){
        mrBillGoesFirst = false;
    }

    public void handleMrBill(){
        mrBillGoesFirst = true;
    }

    public void onPlayClicked() throws Exception {
        if (controller.isMultiplayer()) {
            controller.setPlayer1Name(player1Name.getText());
            controller.setPlayer2Name(player2Name.getText());
        } else {
            if (mrBillGoesFirst) {
                controller.setPlayer1Name("Mr. Bill");
                controller.setPlayer2Name(playerName.getText());
            }else{
                controller.setPlayer2Name("Mr. Bill");
                controller.setPlayer1Name(playerName.getText());
            }

            controller.awakenMrBill(new Node());
            controller.setDifficulty(difficultyCombo.getValue().toString());
            if (mrBillGoesFirst){
                controller.MakeAMove();
            }
        }

        MainApplication.getCoordinator().showGameScene();
    }
}
