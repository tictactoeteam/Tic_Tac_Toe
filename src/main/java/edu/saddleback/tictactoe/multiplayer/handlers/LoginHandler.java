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
 * Handles all login messages and sends the correct responses.
 */
public class LoginHandler implements MessageHandler {

    private Server server;

    public LoginHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        BigInteger sharedSecret = server.getSharedSecret(clientId);

        String encryptedUsername = data.get("username").getAsString();
        String encryptedPassword = data.get("password").getAsString();

        String username = Crypto.decrypt(encryptedUsername, sharedSecret);
        String password = Crypto.decrypt(encryptedPassword, sharedSecret);

        try {
            Player player = PlayerDao.getPlayerByUsername(username);
            if (player == null) {
                sendBadLogin(pubnub, username);
                return;
            }

            if (!player.checkPassword(password)) {
                sendBadLogin(pubnub, username);
                return;
            }

            server.addUser(data.get("UUID").getAsString(), username);
            sendLoggedIn(pubnub, username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendLoggedIn(PubNub pubnub, String username) {
        JsonObject msg = new JsonObject();
        msg.addProperty("type", "loggedIn");

        JsonObject data = new JsonObject();
        data.addProperty("username", username);

        msg.add("data", data);

        try {
            pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        } catch (PubNubException e) {
            e.printStackTrace();
        }
    }

    private void sendBadLogin(PubNub pubnub, String username) {
        JsonObject msg = new JsonObject();
        msg.addProperty("type", "badLogin");

        JsonObject data = new JsonObject();
        data.addProperty("username", username);
        msg.add("data", data);

        try {
            pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        } catch (PubNubException e) {
            e.printStackTrace();
        }
    }
}
