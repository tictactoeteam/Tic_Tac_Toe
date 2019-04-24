package edu.saddleback.tictactoe.controller.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;

public class EndStateHandler implements MessageHandler {
    private GameController controller;

    public EndStateHandler(GameController controller){
        this.controller = controller;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {

    }
}
