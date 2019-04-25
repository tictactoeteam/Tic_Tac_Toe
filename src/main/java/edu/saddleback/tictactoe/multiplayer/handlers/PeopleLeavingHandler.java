package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.db.DbConnection;
import edu.saddleback.tictactoe.db.GameDao;
import edu.saddleback.tictactoe.model.Game;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.multiplayer.Server;

import java.sql.SQLException;

public class PeopleLeavingHandler implements MessageHandler {
    private Server server;
    public PeopleLeavingHandler(Server server){
        this.server = server;
    }
    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        String uuid = data.get("UUID").getAsString();
        String username = server.findUser(uuid);


        System.out.println("HERE!");
        if(username==null) {
            // noob didn't manage to log-in, and left before that
            return;
        }

        Game game = server.findGame(username);

        if (game == null) {
            // noob didn't play a single game
            return;
        }

        try {
            GameDao.insertGame(game);
            System.out.println("SUCCESSFUL SAVE OF A GAME!");
        }catch(SQLException ex){
            ex.printStackTrace();
        }

        JsonObject msg = new JsonObject();
        JsonObject dt = new JsonObject();

        server.removeGame(game);
        System.out.println("REMOVAL OF THE GAME!");

        server.removeUser(uuid);
        System.out.println("REMOVAL OF THE USER!");
    }
}
