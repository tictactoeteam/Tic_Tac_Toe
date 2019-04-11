package edu.saddleback.tictactoe.controller.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.observable.Observable;

public class LoginHandler implements MessageHandler {
    private Observable<Boolean> loggedIn;
    private String attemptedUsername;

    public LoginHandler(Observable<Boolean> loggedIn, String attemptedUsername){
        this.loggedIn = loggedIn;
        this.attemptedUsername = attemptedUsername;
    }

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
