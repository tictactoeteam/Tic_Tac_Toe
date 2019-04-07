package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.db.PlayerDao;
import edu.saddleback.tictactoe.model.Player;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.multiplayer.Server;
import edu.saddleback.tictactoe.util.Crypto;

import java.math.BigInteger;
import java.sql.SQLException;

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

        Player player = new Player();
        player.setUsername(username);
        player.setPassword(password);
        try {
            PlayerDao.insertPlayer(player);
        } catch (SQLException e) {
            System.out.println("Player likely already exists");
        }
    }
}
