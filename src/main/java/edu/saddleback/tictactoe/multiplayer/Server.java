package edu.saddleback.tictactoe.multiplayer;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.sauljohnson.mayo.DiffieHellmanKeyGenerator;
import edu.saddleback.tictactoe.multiplayer.handlers.ConnectHandler;

import java.math.BigInteger;
import java.util.Arrays;

public class Server {
    private String pubKey = System.getenv("TTT_PUBNUB_PUBLISH");
    private String subKey = System.getenv("TTT_PUBNUB_SUBSCRIBE");
    private PubNub pubnub;
    private PNConfiguration pnConfiguration;
    private MessageDelegator delegator;

    private BigInteger privateKey;
    private BigInteger publicKey;

    public Server() {
        this.pnConfiguration = new PNConfiguration();
        this.pnConfiguration.setPublishKey(pubKey);
        this.pnConfiguration.setSubscribeKey(subKey);
        this.pubnub = new PubNub(pnConfiguration);
        this.delegator = new MessageDelegator();
        pubnub.addListener(this.delegator);

        this.privateKey = DiffieHellmanKeyGenerator.generatePrivateKey();
        this.publicKey = DiffieHellmanKeyGenerator.generatePublicKey(this.privateKey);
    }

    public void start() {
        this.delegator.addHandler("connect", new ConnectHandler(privateKey, publicKey));
        pubnub.subscribe().channels(Arrays.asList("main")).withPresence().execute();
    }
}
