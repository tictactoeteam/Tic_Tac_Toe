package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * This object interacts with the login page, handles all error checking, and establishes which type of game is to be
 * generated. handles all possible user input errors.
 */
public class LoginView {
    private ServerConnection conn;

    @FXML
    private Label errorText;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    public LoginView() {
;
    }

    public void onLoginClicked() throws Exception {
        if(!usernameTextField.getText().equals("") && !passwordTextField.getText().equals("")){
            conn = ServerConnection.getInstance(usernameTextField.getText());
            conn.login(usernameTextField.getText(), passwordTextField.getText());
            TicTacToeApplication.getCoordinator().showLobbyScene();
        }else{
            errorText.setText("***error-missing credentials***");
            errorText.setVisible(true);
        }
    }

    public void onCreateAccountClicked(){

        String username = usernameTextField.getText();
        if(!username.equals("") && !passwordTextField.getText().equals("")) {
            conn.signup(username, passwordTextField.getText());
        }
    }

}
