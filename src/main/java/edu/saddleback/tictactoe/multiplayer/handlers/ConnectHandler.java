package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.sauljohnson.mayo.DiffieHellmanKeyGenerator;
import edu.saddleback.tictactoe.multiplayer.MessageHandler;

import java.math.BigInteger;

public class ConnectHandler implements MessageHandler {
    private BigInteger privateKey;
    private BigInteger publicKey;

    public ConnectHandler(BigInteger privateKey, BigInteger publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Override
    public void handleMessage(JsonObject data, PubNub pubnub, String clientId) {
        BigInteger userPubkey = data.get("publicKey").getAsBigInteger();
        BigInteger sharedSecret = DiffieHellmanKeyGenerator.generateSharedKey(userPubkey, privateKey);

        System.out.println("Server private is " + this.privateKey.toString());
        System.out.println("Server public is " + this.publicKey.toString());
        System.out.println("Shared secret is " + sharedSecret.toString());

        broadcastPublicKey(pubnub);
    }

    private void broadcastPublicKey(PubNub pubnub) {
        JsonObject msg = new JsonObject();
        msg.addProperty("type", "serverPub");

        JsonObject data = new JsonObject();
        data.addProperty("publicKey", this.publicKey);
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
