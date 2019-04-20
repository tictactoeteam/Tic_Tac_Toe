package edu.saddleback.tictactoe.controller.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;

public class GameStartsHandler implements MessageHandler {
    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        String player1 = data.get("player1").getAsString();
        String player2 = data.get("player2").getAsString();

        /*
        switch into the game mode I guess
         */

    }
}
