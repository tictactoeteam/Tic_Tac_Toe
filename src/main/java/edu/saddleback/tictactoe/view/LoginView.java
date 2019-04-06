package edu.saddleback.tictactoe.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import edu.saddleback.tictactoe.controller.GameController;

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

        if(true){//<-SEARCH THROUGH DATABASE IF THE USERNAME AND PASSWORD MATCH AN ACCOUNT, ELSE ERROR
            try{
                TicTacToeApplication.getCoordinator().showLobbyScene();
            }catch(Exception ex){System.out.println("OOF");}
            //Login to the server and join the lobby, show lobby screen

        }else{

            errorText.setText("***error-invalid credentials***");
            errorText.setVisible(true);

        }

    }

    public void onCreateAccountClicked(){

        if(true) {//<-SEARCH THROUGH DATABASE AND CREATE ACCOUNT IF ACCOUNT DOES NOT EXIST

            //Add account to the database, login to the server and join the lobby, show lobby screen

        }else{

            errorText.setText("***error-account already exists***");
            errorText.setVisible(true);

        }

    }

}
