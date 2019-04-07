package edu.saddleback.tictactoe.controller;

import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.sauljohnson.mayo.DiffieHellmanKeyGenerator;
import edu.saddleback.tictactoe.observable.Observable;
import edu.saddleback.tictactoe.util.Crypto;

import java.math.BigInteger;
import java.util.Arrays;

public class ServerConnection {
    private static String pubkey = System.getenv("TTT_PUBNUB_PUBLISH");
    private static String subkey = System.getenv("TTT_PUBNUB_SUBSCRIBE");
    private static ServerConnection instance;

    private BigInteger dhPrivateKey;
    private BigInteger dhPublicKey;

    private Observable<BigInteger> sharedSecret;

    private PubNub pubnub;

    private ServerConnection() {
        PNConfiguration config = new PNConfiguration();
        config.setPublishKey(pubkey);
        config.setSubscribeKey(subkey);

        this.pubnub = new PubNub(config);

        this.dhPrivateKey = DiffieHellmanKeyGenerator.generatePrivateKey();
        this.dhPublicKey = DiffieHellmanKeyGenerator.generatePublicKey(this.dhPrivateKey);

        this.sharedSecret = new Observable<>();

        this.sharedSecret.subscribe(System.out::println);

        waitForServerPub();
        connect();
    }

    private void connect() {
        JsonObject msg = new JsonObject();
        msg.addProperty("type", "connect");

        JsonObject data = new JsonObject();
        data.addProperty("publicKey", this.dhPublicKey);

        msg.add("data", data);

        try {
            this.pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        } catch (PubNubException e) {
            e.printStackTrace();
        }
    }

    private void waitForServerPub() {
        this.pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {}

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                String type = message.getMessage().getAsJsonObject().get("type").getAsString();
                if (!type.equals("serverPub")) {
                    return;
                }

                JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();
                BigInteger serverPub = data.get("publicKey").getAsBigInteger();
                BigInteger secret = DiffieHellmanKeyGenerator.generateSharedKey(serverPub, dhPrivateKey);
                sharedSecret.set(secret);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {}
        });

        this.pubnub.subscribe().channels(Arrays.asList("main")).execute();
    }

    public void login(String username, String password) {
        JsonObject msg = new JsonObject();
        msg.addProperty("type", "login");

        JsonObject data = new JsonObject();
        data.addProperty("username", Crypto.encrypt(username, sharedSecret.get()));
        data.addProperty("password", Crypto.encrypt(password, sharedSecret.get()));

        msg.add("data", data);

        try {
            this.pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        } catch (PubNubException e) {
            e.printStackTrace();
        }
    }

    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }

        return instance;
    }

    public PubNub getPubNub(){return pubnub;}

}
