package edu.saddleback.tictactoe.controller.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.controller.GameController;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.view.TicTacToeApplication;

public class MoveHandler implements MessageHandler {

    GameController controller;

    public MoveHandler(GameController controller){
        this.controller = controller;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        String player1 = data.get("player1").getAsString();
        String player2 = data.get("player2").getAsString();

        System.out.println(player1);
        System.out.println(player2);

        System.out.println(controller.getPlayer1Name().equals(player1)
                && controller.getPlayer2Name().equals(player2)
                ||
                controller.getPlayer1Name().equals(player2)
                        && controller.getPlayer2Name().equals(player1));

        if (controller.getPlayer1Name().equals(player1)
        && controller.getPlayer2Name().equals(player2)
        ||
        controller.getPlayer1Name().equals(player2)
        && controller.getPlayer2Name().equals(player1)){

            System.out.println("HERE!!!");

            BoardMove move;

            int row = data.get("position").getAsInt() / 3;
            int col = data.get("position").getAsInt() % 3;

            GamePiece piece;

            if (data.get("piece").getAsString().equals("X"))
                piece = GamePiece.X;
            else
                piece = GamePiece.O;

            move = new BoardMove(row, col, piece);

            controller.applyMove(move);
        }
    }
}
