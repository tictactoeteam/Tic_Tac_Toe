package edu.saddleback.tictactoe.view;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import edu.saddleback.tictactoe.controller.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import edu.saddleback.tictactoe.controller.GameController;
import java.util.Arrays;

/**
 * This object interacts with the login page, handles all error checking, and establishes which type of game is to be
 * generated. handles all possible user input errors.
 */
public class LoginView {

    private GameController controller;
    private ServerConnection conn;
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
        conn = ServerConnection.getInstance();
    }

    public void onLoginClicked(){

        if(!usernameTextField.getText().equals("") && !passwordTextField.getText().equals("")){

            conn.login(usernameTextField.getText(), passwordTextField.getText());

            conn.getPubNub().addListener(new SubscribeCallback() {
                @Override
                public void status(PubNub pubnub, PNStatus status) {}
                @Override
                public void message(PubNub pubnub, PNMessageResult message) {

                    String messageType = message.getMessage().getAsJsonObject().get("type").getAsString();//Message type
                    JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();
                    String userN = data.get("username").getAsString();//Returned username

                    if (userN.equals(usernameTextField.getText()) && messageType.equals("loggedIn")){//Success

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        TicTacToeApplication.getCoordinator().showLobbyScene();
                                        System.out.println("Good login");
                                    }catch(Exception ex){ex.printStackTrace();}
                                }
                            });

                    }else if(userN.equals(usernameTextField.getText()) && messageType.equals("badLogin")) {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                errorText.setText("***error-invalid credentials***");
                                errorText.setVisible(true);
                            }
                        });

                    }

                }

                @Override
                public void presence(PubNub pubnub, PNPresenceEventResult presence) {}
            });

            conn.getPubNub().subscribe().channels(Arrays.asList("main")).execute();

        }else{

            errorText.setText("***error-missing credentials***");
            errorText.setVisible(true);

        }

    }

    public void onCreateAccountClicked(){

        String initialUsername = usernameTextField.getText();


        if(!initialUsername.equals("") && !passwordTextField.getText().equals("")) {

            conn.signup(initialUsername, passwordTextField.getText());

            conn.getPubNub().addListener(new SubscribeCallback() {
                @Override
                public void status(PubNub pubnub, PNStatus status) {
                }

                @Override
                public void message(PubNub pubnub, PNMessageResult message) {

                    String messageType = message.getMessage().getAsJsonObject().get("type").getAsString();//Message type
                    JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();
                    String userN = data.get("username").getAsString();//Returned username
                    System.out.println("Returned UserN: " + userN);
                    if (userN.equals(initialUsername) && messageType.equals("accountCreated")) {//Success
                        System.out.println("SUCCESS ENTERED");
                        conn.login(usernameTextField.getText(), passwordTextField.getText());

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    TicTacToeApplication.getCoordinator().showLobbyScene();
                                    System.out.println("Good login");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                    } else if (userN.equals(usernameTextField.getText()) && messageType.equals("accountExists")) {
                        System.out.println("EXISTS ENTERED");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                errorText.setText("***error-account already exists***");
                                errorText.setVisible(true);
                            }
                        });

                    }

                }

                @Override
                public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                }
            });

            conn.getPubNub().subscribe().channels(Arrays.asList("main")).execute();
        }
    }

}
