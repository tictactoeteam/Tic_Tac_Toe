package edu.saddleback.tictactoe.controller.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;

public class RegisterHandler implements MessageHandler {
    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {

    }
}
