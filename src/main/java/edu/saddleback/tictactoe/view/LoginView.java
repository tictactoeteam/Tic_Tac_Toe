package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.decision.Node;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import edu.saddleback.tictactoe.controller.GameController;

/**
 * This object interacts with the login page, handles all error checking, and establishes which type of game is to be
 * generated. handles all possible user input errors.
 */
public class LoginView {

    private boolean mrBillGoesFirst;
    private GameController controller;

    @FXML
    private HBox radioHbox;
    @FXML
    private RadioButton radioPlayer;
    @FXML
    private RadioButton radioBill;
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
    @FXML
    private ComboBox multiplayerComboBox;
    @FXML
    private ComboBox onlineTypeComboBox;
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField joinCodeTextField;

    /**
     * Initializes the controller in the login view
     */
    public void initialize() {
        controller = TicTacToeApplication.getController();
    }


}
