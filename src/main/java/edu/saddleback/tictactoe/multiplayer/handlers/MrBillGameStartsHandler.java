package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.multiplayer.Server;

public class MrBillGameStartsHandler implements MessageHandler {
    private Server server;

    public MrBillGameStartsHandler(Server server){
        this.server = server;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        if (!data.get("player1Username").getAsString().equals("Mr. Bill")){
            return;
        }

        BoardMove move = server.MrBillMakesMove(server.findGame(data.get("player2Username").getAsString()));


        JsonObject msg = new JsonObject();

        msg.addProperty("type", "move");

        JsonObject dt = new JsonObject();

        dt.addProperty("position", move.getRow()*3 + move.getCol());
        dt.addProperty("player1", "Mr. Bill");
        dt.addProperty("player2", data.get("player2Username").getAsString());
        dt.addProperty("piece", "X");



    }
}
