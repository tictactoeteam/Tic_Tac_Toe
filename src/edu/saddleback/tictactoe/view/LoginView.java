package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.MainApplication;
import edu.saddleback.tictactoe.decision.Node;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import edu.saddleback.tictactoe.controller.GameController;

/**
 * This object interacts with the login page, handles all error checking, and establishes which type of game is to be
 * generated.
 */
public class LoginView {

    private GameController controller;
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
    @FXML
    private Label errorText;

    /**
     * Initializes the controller in the login view
     */
    public void initialize() {
        controller = MainApplication.getController();
    }

    /**
     * Controls which ui components should be displayed.
     * @param multiplayer
     */
    public void setMultiplayer(boolean multiplayer) {
        playerNamesBox.setVisible(multiplayer);
        singlePlayerNameBox.setVisible(!multiplayer);
        difficultyCombo.setVisible(!multiplayer);

        controller.setMultiplayer(multiplayer);
    }

    /**
     * Sets multiplayer to false.
     */
    public void onSingleplayerClicked(){setMultiplayer(false);}

    /**
     * Sets multiplayer to true.
     */
    public void onMultiplayerClicked(){setMultiplayer(true);}

    /**
     * Places the loading screens, must be done here and not in the onPlayClicked() method because the processor is
     * overloaded generating the tree, this is the work around.
     */
    public void onPlayPressed(){

        if(controller.isMultiplayer()){

            if(!player1Name.getText().trim().equals("") && !player2Name.getText().trim().equals("")){

                errorText.setText("Loading...");
                errorText.setVisible(true);

            }

        }else{

            if(!difficultyCombo.getSelectionModel().isEmpty() && !playerName.getText().trim().equals("")){

                if(difficultyCombo.getValue().toString().equals("Easy Mode"))
                    errorText.setText("Loading easy AI...");
                else
                    errorText.setText("Waking Mr. Bill from his 1000 year slumber...");

                errorText.setVisible(true);

            }

        }

    }

    /**
     * Preforms all error checks to see if the user entered all needed information into the login screen, else spits
     * out an error message. If all is correct, the specified game is generated.
     * @throws Exception
     */
    public void onPlayClicked() throws Exception {

        if (controller.isMultiplayer()) {

            if(!player1Name.getText().trim().equals("") && !player2Name.getText().trim().equals("")){

                controller.setPlayer1Name(player1Name.getText());
                controller.setPlayer2Name(player2Name.getText());
                MainApplication.getCoordinator().showGameScene();

            }else{

                errorText.setText("***error - please enter all info***");
                errorText.setVisible(true);

            }

        } else {

            if(!difficultyCombo.getSelectionModel().isEmpty() && !playerName.getText().trim().equals("")){

                controller.setPlayer1Name(playerName.getText());
                controller.awakenMrBill(new Node());
                controller.setDifficulty(difficultyCombo.getValue().toString());

                if(difficultyCombo.getValue().toString().equals("Easy Mode"))
                    controller.setPlayer2Name("My 3-Year Old Sister");
                else
                    controller.setPlayer2Name("Mr. Bill");

                MainApplication.getCoordinator().showGameScene();

            }else {

                errorText.setText("***error - please enter all info***");
                errorText.setVisible(true);

            }

        }

    }
}
