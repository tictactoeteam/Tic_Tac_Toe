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
import edu.saddleback.tictactoe.controller.handlers.LoginHandler;
import edu.saddleback.tictactoe.controller.handlers.RegisterHandler;
import edu.saddleback.tictactoe.controller.handlers.ServerPubHandler;
import edu.saddleback.tictactoe.model.JsonMove;
import edu.saddleback.tictactoe.multiplayer.MessageDelegator;
import edu.saddleback.tictactoe.observable.Observable;
import edu.saddleback.tictactoe.util.Crypto;

import java.math.BigInteger;
import java.util.Arrays;

public class ServerConnection {
    private static String pubkey = System.getenv("TTT_PUBNUB_PUBLISH");
    private static String subkey = System.getenv("TTT_PUBNUB_SUBSCRIBE");
    private static ServerConnection instance;

    private MessageDelegator delegator;

    private BigInteger dhPrivateKey;
    private BigInteger dhPublicKey;

    private String attemptedUsername;

    private Observable<BigInteger> sharedSecret;
    private Observable<Boolean> loggedIn;

    private PubNub pubnub;

    private ServerConnection() {
        PNConfiguration config = new PNConfiguration();
        config.setPublishKey(pubkey);
        config.setSubscribeKey(subkey);

        this.pubnub = new PubNub(config);

        this.dhPrivateKey = DiffieHellmanKeyGenerator.generatePrivateKey();
        this.dhPublicKey = DiffieHellmanKeyGenerator.generatePublicKey(this.dhPrivateKey);

        this.sharedSecret = new Observable<>();
        this.loggedIn = new Observable<>();

        this.delegator = new MessageDelegator();
        this.delegator.addHandler("loginResponse", new LoginHandler(loggedIn, attemptedUsername));
        this.delegator.addHandler("registerResponse", new RegisterHandler());
        this.delegator.addHandler("serverPub", new ServerPubHandler());

        waitForServerPub();
        connect();
        registerLoginStatusListener();
    }

    private void registerLoginStatusListener() {
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {}
            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                String type = message.getMessage().getAsJsonObject().get("type").getAsString();//Message type
                JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();

                if (!Arrays.asList("loggedIn", "accountCreated", "badLogin", "accountExists").contains(type)) {
                    return;
                }

                String username = data.get("username").getAsString();

                if (type.equals("loggedIn") && username.equals(attemptedUsername)) {
                    loggedIn.set(true);
                }

                if (type.equals("accountCreated") && username.equals(attemptedUsername)) {
                    loggedIn.set(true);
                }

                if (type.equals("badLogin") && username.equals(attemptedUsername)) {
                    loggedIn.set(false);
                }

                if (type.equals("accountExists") && username.equals(attemptedUsername)) {
                    loggedIn.set(false);
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {}
        });
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
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                if (presence.getEvent().equals("join")) {
                    System.out.println("Player joined");
                    presence.getHereNowRefresh();
                    presence.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    presence.getTimestamp(); // 1345546797
                    presence.getOccupancy(); // 2
                }
                else if(presence.getEvent().equals("leave")){
                    System.out.println("Player left");
                    presence.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    presence.getTimestamp(); // 1345546797
                    presence.getOccupancy(); // 2
                }
                else{
                    System.out.println("Timed out");
                    presence.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    presence.getTimestamp(); // 1345546797
                    presence.getOccupancy(); // 2
                }
                System.out.println(pubnub.hereNow());
            }
        });

        this.pubnub.subscribe().channels(Arrays.asList("main")).execute();
    }

    public void login(String username, String password) {
        this.attemptedUsername = username;
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

    public void signup(String username, String password) {
        JsonObject msg = new JsonObject();
        msg.addProperty("type", "signup");

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

    public Observable<Boolean> getLoggedInObservable() {
        return loggedIn;
    }

    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }

        return instance;
    }

    public void sendMessage(int row, int col){
        JsonObject msg = JsonMove.convertToJson(row, col);

        try{
            this.pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        }catch(PubNubException e){
            e.printStackTrace();
        }
    }

    public PubNub getPubNub(){return pubnub;}

}