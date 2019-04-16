package edu.saddleback.tictactoe.controller.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.view.TicTacToeApplication;

public class MoveHandler implements MessageHandler {


    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        TicTacToeApplication.getController().applyJsonMove(data);
    }
}
