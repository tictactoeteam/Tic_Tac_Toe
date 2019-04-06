package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.multiplayer.Server;
import edu.saddleback.tictactoe.util.Crypto;

import java.math.BigInteger;

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

        System.out.println("Encrypted " + encryptedUsername + " " + encryptedPassword);
        System.out.println("Trying to login " + username + " " + password);
    }
}
