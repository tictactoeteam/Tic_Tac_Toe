package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import edu.saddleback.tictactoe.db.GameDao;
import edu.saddleback.tictactoe.model.Game;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import java.sql.SQLException;

public class GameDaoHandler implements MessageHandler {

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {


        JsonObject msg = new JsonObject();
        msg.addProperty("type", "returnedGames");

        JsonObject gameData = new JsonObject();
        System.out.println("GOT MESSAGE");
        Game[] games;
        //Finds and packages data
        try {
            games = GameDao.getAllGames();

            JsonArray player1NameArray = new JsonArray();
            JsonArray player2NameArray = new JsonArray();
            JsonArray moves = new JsonArray();

            //Fills JsonArrays with the retrieved data.
            for(int i = 0; i < games.length; i++){

                player1NameArray.add(games[i].getPlayerX().getUsername());
                player2NameArray.add(games[i].getPlayerO().getUsername());
                JsonArray tmpGameMoves = new JsonArray();
                for(int j = 0; j < 9; j++){
                    tmpGameMoves.add(games[i].getMove(j));
                }
                moves.add(tmpGameMoves);

            }
            gameData.add("playerX", player1NameArray);
            gameData.add("playerO", player2NameArray);
            gameData.add("moves", moves);

            msg.add("data", gameData);
            System.out.println("THIS IS THE TYPE" + msg.get("type").getAsString());
            try {
                pubnub.publish()
                        .channel("main")
                        .message(msg)
                        .sync();
            }catch(PubNubException ex){
                ex.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("GAME RETREIVAL FAILED");
        }

    }
}
