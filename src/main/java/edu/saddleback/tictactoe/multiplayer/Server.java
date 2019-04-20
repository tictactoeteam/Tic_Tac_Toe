package edu.saddleback.tictactoe.multiplayer;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.sauljohnson.mayo.DiffieHellmanKeyGenerator;
import edu.saddleback.tictactoe.model.Game;
import edu.saddleback.tictactoe.multiplayer.handlers.ConnectHandler;
import edu.saddleback.tictactoe.multiplayer.handlers.LoginHandler;
import edu.saddleback.tictactoe.multiplayer.handlers.MoveValidateHandler;
import edu.saddleback.tictactoe.multiplayer.handlers.SignupHandler;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class Server {
    private String pubKey = System.getenv("TTT_PUBNUB_PUBLISH");
    private String subKey = System.getenv("TTT_PUBNUB_SUBSCRIBE");
    private PubNub pubnub;
    private PNConfiguration pnConfiguration;
    private MessageDelegator delegator;

    private Vector<Game> gamesPlayed;

    private BigInteger privateKey;
    private BigInteger publicKey;
    private HashMap<String, BigInteger> sharedSecrets;

    public Server() {
        gamesPlayed = new Vector<>();
        this.pnConfiguration = new PNConfiguration();
        this.pnConfiguration.setPublishKey(pubKey);
        this.pnConfiguration.setSubscribeKey(subKey);
        this.pnConfiguration.setUuid("someUsername"); //Get the users display name for the UUID
        this.pubnub = new PubNub(pnConfiguration);
        this.delegator = new MessageDelegator();
        pubnub.addListener(this.delegator);

        this.privateKey = DiffieHellmanKeyGenerator.generatePrivateKey();
        this.publicKey = DiffieHellmanKeyGenerator.generatePublicKey(this.privateKey);
        this.sharedSecrets = new HashMap<>();
    }

    public void addSharedSecret(String clientId, BigInteger secret) {
        this.sharedSecrets.put(clientId, secret);
    }

    public BigInteger getSharedSecret(String clientId) {
        return this.sharedSecrets.get(clientId);
    }

    public void start() {
        this.delegator.addHandler("connect", new ConnectHandler(this, privateKey, publicKey));
        this.delegator.addHandler("connect", new ConnectHandler(this, privateKey, publicKey));
        this.delegator.addHandler("login", new LoginHandler(this));
        this.delegator.addHandler("signup", new SignupHandler(this));
        this.delegator.addHandler("makeMove", new MoveValidateHandler());
        pubnub.subscribe().channels(Arrays.asList("main")).withPresence().execute();
    }
}
