package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.model.*;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.multiplayer.Server;

/**
 * Handles the move validation messages and send a response message.
 */
public class MoveValidateHandler implements MessageHandler {

    private Server server;
    private AdvancedEvaluator winnerChecker = new AdvancedEvaluator();


    public MoveValidateHandler(Server server){
       this.server = server;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {

        System.out.println("MESSAGE RECEIVED!!!");

        try {

            System.out.println(data.get("player1").getAsString());
            System.out.println(data.get("player2").getAsString());
            Game game = server.findGame(data.get("player1").getAsString(), data.get("player2").getAsString());
            System.out.println("Game was found!!!");
            Board board = game.getBoard();

            System.out.println("Board Before: " + board);

            int pos = data.get("position").getAsInt();

            int row = pos / 3;
            int col = pos % 3;

            GamePiece piece;
            if (data.get("piece").getAsString().equals("X")) {
                piece = GamePiece.X;
            } else {
                piece = GamePiece.O;
            }

            System.out.println("PIECE RECEIVED: " +  data.get("piece").getAsString());

            BoardMove move = new BoardMove(row, col, piece);
            System.out.println("move initialized!");
            JsonObject msg = new JsonObject();
            JsonObject dt = new JsonObject();
            try {
                if (piece == GamePiece.O && board.isXTurn()
                || piece == GamePiece.X && board.isOTurn()){
                    throw new NotYourTurnException();
                }

                if(winnerChecker.evaluate(board) > 0){
                    throw new EndStateException(data.get("player1").getAsString(), data.get("player2").getAsString(), false);
                }
                if (winnerChecker.evaluate(board) < 0){
                    throw new EndStateException(data.get("player2").getAsString(), data.get("player1").getAsString(), false);
                }
                if (board.getTurnNumber() == 9){
                    throw new EndStateException(data.get("player1").getAsString(), data.get("player2").getAsString(), true);
                }

                move.applyTo(board);

                System.out.println("Board After: " + board);

                msg.addProperty("type", "moveResp");

                dt.addProperty("position", pos);
                dt.addProperty("piece", data.get("piece").getAsString());
                dt.addProperty("player1", data.get("player1").getAsString());
                dt.addProperty("player2", data.get("player2").getAsString());


                msg.add("data", dt);

            } catch (GridAlreadyChosenException ex) {
                msg.addProperty("type", "moveResp");
                msg.add("data", new JsonObject());
            }
            catch(NotYourTurnException ex){
                msg.addProperty("type", "moveResp");
                msg.add("data", new JsonObject());
            }
            catch(EndStateException ex){

                if (ex.isDrawn()){//Draw
                    msg.addProperty("type", "endState");
                    dt.addProperty("player1", ex.getWinner());
                    dt.addProperty("player2", ex.getLoser());
                    msg.add("data", dt);
                }
                else{//Win/Loss
                    msg.addProperty("type", "endState");
                    dt.addProperty("winner", ex.getWinner());
                    dt.addProperty("loser", ex.getLoser());
                    msg.add("data", dt);
                }
            }

            try {
                pubnub.publish()
                        .channel("main")
                        .message(msg)
                        .sync();
            } catch (PubNubException ex) {
                ex.printStackTrace();
            }

        }catch(NullPointerException ex){
            System.out.println("Game Not Found! You suck!");
        }

    }

}
