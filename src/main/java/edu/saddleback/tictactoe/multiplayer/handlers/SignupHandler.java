package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import edu.saddleback.tictactoe.db.PlayerDao;
import edu.saddleback.tictactoe.model.Player;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.multiplayer.Server;
import edu.saddleback.tictactoe.util.Crypto;
import java.math.BigInteger;
import java.sql.SQLException;

/**
 * Handles the possible signup messages and responds.
 */
public class SignupHandler implements MessageHandler {
    private Server server;

    public SignupHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        BigInteger sharedSecret = server.getSharedSecret(clientId);

        String encryptedUsername = data.get("username").getAsString();
        String encryptedPassword = data.get("password").getAsString();

        String username = Crypto.decrypt(encryptedUsername, sharedSecret);
        String password = Crypto.decrypt(encryptedPassword, sharedSecret);
        try{
            Player player = PlayerDao.getPlayerByUsername(username);
            if(player != null) { // Found player in database
                sendAccountExists(pubnub, username);
                return;
            }

            player = new Player();
            player.setUsername(username);
            player.setPassword(password);
            //We need a way of setting the pubnub id to this setID method... see if we can get the pubnub id in this
            //function of the cleint currently connected
          //  player.setId();
            PlayerDao.insertPlayer(player);
            sendAccountCreated(pubnub, username);
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void sendAccountCreated(PubNub pubnub, String username){

        JsonObject msg = new JsonObject();
        msg.addProperty("type", "accountCreated");

        JsonObject data = new JsonObject();
        data.addProperty("username", username);

        msg.add("data", data);

        try{
            pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        }catch(PubNubException ex){ex.printStackTrace();}

    }

    private void sendAccountExists(PubNub pubnub, String username){

        JsonObject msg = new JsonObject();
        msg.addProperty("type", "accountExists");

        JsonObject data = new JsonObject();
        data.addProperty("username", username);

        msg.add("data", data);

        try{
            pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        }catch(PubNubException ex){ex.printStackTrace();}

    }

}
