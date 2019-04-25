package edu.saddleback.tictactoe.controller.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.view.TicTacToeApplication;

public class EndStateHandler implements MessageHandler {
    private GameController controller;

    public EndStateHandler(GameController controller){
        this.controller = controller;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {



            // PLEASE SOMEONE DO SOMETHING WITH THIS!!! <3
        //        // We have to make sure that the message is intended to you,
        //        // The specification of the message is at MoveValidateHandler line 92-100, change it if you want
        //        // ..... yeah, looks simple enough

    }
}
