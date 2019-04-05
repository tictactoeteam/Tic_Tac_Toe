package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.db.PlayerDao;
import edu.saddleback.tictactoe.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import edu.saddleback.tictactoe.controller.GameController;
import java.sql.SQLException;

/**
 * This object interacts with the login page, handles all error checking, and establishes which type of game is to be
 * generated. handles all possible user input errors.
 */
public class LoginView {

    private GameController controller;
    @FXML
    private Label errorText;
    @FXML
    private Button loginButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    /**
     * Initializes the controller in the login view
     */
    public void initialize() {
        controller = TicTacToeApplication.getController();
    }

    public void onLoginClicked(){

        try {
            Player tmpPlayer = PlayerDao.getPlayerByUsername(usernameTextField.getText());

            if(tmpPlayer != null && tmpPlayer.checkPassword(passwordTextField.getText())){

                //Login to the server and join the lobby, show lobby screen

            }else{

                errorText.setText("***error-invalid credentials***");
                errorText.setVisible(true);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void onCreateAccountClicked(){

        try {
            Player tmpPlayer = PlayerDao.getPlayerByUsername(usernameTextField.getText());

            if(tmpPlayer == null){

                PlayerDao.insertPlayer(new Player(usernameTextField.getText(), passwordTextField.getText()));
                //Login to the server and join the lobby, show lobby screen

            }else{

                errorText.setText("***error-account username already exists***");
                errorText.setVisible(true);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        if(true) {//<-SEARCH THROUGH DATABASE AND CREATE ACCOUNT IF ACCOUNT DOES NOT EXIST

            //Add account to the database, login to the server and join the lobby, show lobby screen

        }else{

            errorText.setText("***error-account already exists***");
            errorText.setVisible(true);

        }

    }

}
