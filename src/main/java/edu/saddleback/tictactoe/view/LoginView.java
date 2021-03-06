package edu.saddleback.tictactoe.view;

import edu.saddleback.tictactoe.controller.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    /**
     * Initializes connection to the server and holds the success login listener.
     */
    public LoginView() {

        conn = ServerConnection.getInstance();
        conn.getLoggedInObservable().subscribe((onLoginChanged) -> {
            if (onLoginChanged.equals(true)) {
                Platform.runLater(() -> {
                    try {
                        TicTacToeApplication.getCoordinator().showLobbyScene();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                Platform.runLater(() -> {
                    errorText.setText("***error-bad login***");
                    errorText.setVisible(true);
                });
            }
        });

    }

    /**
     * Runs when a login attempt is made.
     * @throws Exception
     */
    public void onLoginClicked() throws Exception {
        if(!usernameTextField.getText().equals("") && !passwordTextField.getText().equals("")){
            conn.login(usernameTextField.getText(), passwordTextField.getText());

        }else{
            errorText.setText("***error-missing credentials***");
            errorText.setVisible(true);
        }
    }

    /**
     * Runs when a create account attempt is made.
     */
    public void onCreateAccountClicked(){

        String username = usernameTextField.getText();
        if(!username.equals("") && !passwordTextField.getText().equals("")) {
            conn.signup(username, passwordTextField.getText());
        }
    }

}
