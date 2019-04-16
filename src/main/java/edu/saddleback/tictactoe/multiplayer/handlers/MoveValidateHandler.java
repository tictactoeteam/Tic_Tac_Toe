package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import edu.saddleback.tictactoe.model.*;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;

public class MoveValidateHandler implements MessageHandler {

    private Board board;


    public MoveValidateHandler(){
        board = new Board();
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        int pos = data.get("position").getAsInt();

        int row = pos/3;
        int col = pos%3;

        GamePiece piece;
        if(data.get("piece").getAsString() == "X"){
            piece = GamePiece.X;
        }else{
            piece = GamePiece.O;
        }

        BoardMove move = new BoardMove(row, col, piece);
        JsonObject msg = new JsonObject();
        try{
            move.applyTo(board);

            msg = new JsonObject();
            msg.addProperty("type", "move");
            JsonObject dt = new JsonObject();
            dt.addProperty("position", pos);
            dt.addProperty("piece", data.get("piece").getAsString());

            msg.add("data", dt);

        }catch(GridAlreadyChosenException ex){
            msg.addProperty("type", "move");
        }

        try{
            pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        }catch(PubNubException ex){
            ex.printStackTrace();
        }

    }
}
