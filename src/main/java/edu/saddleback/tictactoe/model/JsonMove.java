package edu.saddleback.tictactoe.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonMove {
    private JsonObject theMove;

    private JsonMove(int row, int col){
        theMove = new JsonObject();

        theMove.addProperty("type" , "makeMove");
        JsonObject data = new JsonObject();
        data.addProperty("position", row*3+col);
        data.addProperty("piece", "X");
        theMove.add("data", data);

    }

    private JsonObject getTheMove(){
        return theMove;
    }

    public static JsonObject convertToJson(int row, int col){
        return new JsonMove(row, col).getTheMove();
    }


    public static BoardMove convertToBoardMove(JsonObject moveResponse){
        int position = moveResponse.get("position").getAsInt();
        GamePiece piece;
        if (moveResponse.get("data").getAsJsonObject().get("player").getAsString() == "X")
            piece = GamePiece.X;
        else
            piece = GamePiece.O;

        int row = position%3;
        int col = position/3;

        return new BoardMove(row, col, piece);
    }
}
