package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.multiplayer.Server;
import edu.saddleback.tictactoe.util.Crypto;

/**
 * Handles the challenge messages and returns the usernames of the game.
 */
public class ChallengeHandler implements MessageHandler {

    private Server server;

    public ChallengeHandler(Server server){
        this.server = server;

    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {

        String p1 = Crypto.decrypt(data.get("player1Username").getAsString(), server.getSharedSecret(clientId));
        String p2 = Crypto.decrypt(data.get("player2Username").getAsString(), server.getSharedSecret(clientId));

        JsonObject msg = new JsonObject();
        if (server.findGame(p1) != null || server.findGame(p2) != null) {
            msg.addProperty("type", "challengeDenied");
        }else {
            server.createGame(p1, p2);
            msg.addProperty("type", "challengeAccepted");

        }
        JsonObject dt = new JsonObject();
        dt.addProperty("player1Username", p1);
        dt.addProperty("player2Username", p2);
        msg.add("data", dt);


        try {
            pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        }catch(PubNubException ex){
            ex.printStackTrace();
        }

    }
}
