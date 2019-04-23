package edu.saddleback.tictactoe.multiplayer;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.sauljohnson.mayo.DiffieHellmanKeyGenerator;
import edu.saddleback.tictactoe.model.Game;
import edu.saddleback.tictactoe.multiplayer.handlers.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

public class Server {
    private String pubKey = System.getenv("TTT_PUBNUB_PUBLISH");
    private String subKey = System.getenv("TTT_PUBNUB_SUBSCRIBE");
    private PubNub pubnub;
    private PNConfiguration pnConfiguration;
    private MessageDelegator delegator;

    private Vector<Game> gamesPlayed;

    private HashMap<String, String> userMap;

    private BigInteger privateKey;
    private BigInteger publicKey;
    private HashMap<String, BigInteger> sharedSecrets;

    public Server() {
        gamesPlayed = new Vector<>();
        userMap = new HashMap<>();
        this.pnConfiguration = new PNConfiguration();
        this.pnConfiguration.setPublishKey(pubKey);
        this.pnConfiguration.setSubscribeKey(subKey);
        //this.pnConfiguration.setUuid("someUsername"); //Get the users display name for the UUID
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

    public void createGame(String player1, String player2){
        gamesPlayed.add(new Game(player1,player2));
    }

    public Game findGame(String player1, String player2){
        for (Game game : gamesPlayed){
            if (game.getPlayerX().getUsername().equals(player1) && game.getPlayerO().getUsername().equals(player2)
            ||  game.getPlayerO().getUsername().equals(player1) && game.getPlayerX().getUsername().equals(player2)){
                return game;
            }
        }
        return null;
    }

    public void addToUsers(String id, String username){
        userMap.put(id, username);
    }

    public String findUser(String id){
        return userMap.get(id);
    }

    public void removeUser(String id){
        userMap.remove(id);
    }

    public void start() {
        this.delegator.addHandler("connect", new ConnectHandler(this, privateKey, publicKey));
        this.delegator.addHandler("connect", new ConnectHandler(this, privateKey, publicKey));
        this.delegator.addHandler("login", new LoginHandler(this));
        this.delegator.addHandler("signup", new SignupHandler(this));
        this.delegator.addHandler("move", new MoveValidateHandler(this));
        this.delegator.addHandler("challenge", new ChallengeHandler(this));
        this.delegator.addHandler("getAllGames", new GameDaoHandler());
        pubnub.subscribe().channels(Arrays.asList("main")).withPresence().execute();
    }
}
