package edu.saddleback.tictactoe.multiplayer;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;

public interface MessageHandler {
    void handleMessage(JsonObject data, PubNub pubnub);
}
