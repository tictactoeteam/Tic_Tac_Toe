package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.Game;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.multiplayer.Server;

public class MrBillMoveHandler implements MessageHandler {
    private Server server;

    public MrBillMoveHandler(Server server){
        this.server = server;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {

        String HopefullyMrBill = data.get("player1").getAsString();
        String player2 = data.get("player2").getAsString();
        if (!HopefullyMrBill.equals("Mr. Bill") && player2.equals("Mr. Bill")){
            String temp = player2;
            player2 = HopefullyMrBill;
            HopefullyMrBill = temp;
        }

        Game game = server.findGame(player2);

        if (HopefullyMrBill.equals("Mr. Bill") && game.getBoard().isXTurn()){
            BoardMove move = server.MrBillMakesMove(game);

            JsonObject msg = new JsonObject();
            JsonObject dt = new JsonObject();

            msg.addProperty("type", "move");
            dt.addProperty("position", move.getRow()*3 + move.getCol());
            dt.addProperty("piece", "X");
            dt.addProperty("player1", "Mr. Bill");
            dt.addProperty("player2", player2);
            msg.add("data", dt);

            try{
                pubnub.publish()
                        .channel("main")
                        .message(msg)
                        .sync();
            }catch(PubNubException ex){
                ex.printStackTrace();
            }

        }else{
            return;
        }
    }
}
