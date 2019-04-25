package edu.saddleback.tictactoe.controller.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.observable.Observable;

/**
 * Handles the responses from the login attempt
 */
public class LoginHandler implements MessageHandler {

    private Observable<Boolean> loggedIn;
    private String attemptedUsername;

    /**
     * Constructor
     * @param loggedIn
     * @param attemptedUsername
     */
    public LoginHandler(Observable<Boolean> loggedIn, String attemptedUsername){
        this.loggedIn = loggedIn;
        this.attemptedUsername = attemptedUsername;
    }

    /**
     * Updates the observable boolean that is true if the login has been validated.
     * @param data
     * @param pubnub
     * @param clientId
     */
    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        boolean success = data.get("success").getAsBoolean();
        String username = data.get("username").getAsString();

        if (success && username.equals(attemptedUsername)){
            loggedIn.set(true);
        }

        if (!success && username.equals(attemptedUsername)){
            loggedIn.set(false);
        }
    }
}
