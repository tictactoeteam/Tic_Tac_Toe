package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;
import edu.saddleback.tictactoe.multiplayer.Server;
import edu.saddleback.tictactoe.util.Crypto;

public class ChallengeHandler implements MessageHandler {

    private Server server;

    public ChallengeHandler(Server server){
        this.server = server;

    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {

        String p1 = Crypto.decrypt(data.get("player1Username").getAsString(), server.getSharedSecret(clientId));
        String p2 = Crypto.decrypt(data.get("player2Username").getAsString(), server.getSharedSecret(clientId));

        server.createGame(p1, p2);

        JsonObject msg = new JsonObject();
        msg.addProperty("type", "challengeAccepted");


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