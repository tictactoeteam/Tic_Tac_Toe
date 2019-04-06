package edu.saddleback.tictactoe.multiplayer.handlers;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
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

        pubnub.publish()
                .message(getMessage(sharedSecret, clientId))
                .channel("main");
    }

    private JsonObject getMessage(BigInteger sharedSecret, String clientId) {
        JsonObject object = new JsonObject();
        object.addProperty("type", "sharedSecret");

        JsonObject data = new JsonObject();
        data.addProperty("secret", sharedSecret);
        data.addProperty("client", clientId);
        object.add("data", data);

        return object;
    }
}
