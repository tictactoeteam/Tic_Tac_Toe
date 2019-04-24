package edu.saddleback.tictactoe.multiplayer;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.sauljohnson.mayo.DiffieHellmanKeyGenerator;
import edu.saddleback.tictactoe.model.Game;
import edu.saddleback.tictactoe.multiplayer.handlers.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * This is the main server, responsible for all database related messages including logins and running games.
 */
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

    /**
     * Constructor
     */
    public Server() {
        gamesPlayed = new Vector<>();
        this.pnConfiguration = new PNConfiguration();
        this.pnConfiguration.setPublishKey(pubKey);
        this.pnConfiguration.setSubscribeKey(subKey);
        this.pubnub = new PubNub(pnConfiguration);
        this.delegator = new MessageDelegator();
        pubnub.addListener(this.delegator);

        this.privateKey = DiffieHellmanKeyGenerator.generatePrivateKey();
        this.publicKey = DiffieHellmanKeyGenerator.generatePublicKey(this.privateKey);
        this.sharedSecrets = new HashMap<>();
    }

    /**
     * Adds a shared secret to the server.
     * @param clientId
     * @param secret
     */
    public void addSharedSecret(String clientId, BigInteger secret) {
        this.sharedSecrets.put(clientId, secret);
    }

    /**
     * Returns the shared secret.
     * @param clientId
     * @return
     */
    public BigInteger getSharedSecret(String clientId) {
        return this.sharedSecrets.get(clientId);
    }

    /**
     * Creates a new game between two users on the server.
     * @param player1
     * @param player2
     */
    public void createGame(String player1, String player2){
        gamesPlayed.add(new Game(player1,player2));
    }

    /**
     * Returns a game from two given usernames.
     * @param player1
     * @param player2
     * @return
     */
    public Game findGame(String player1, String player2){
        for (Game game : gamesPlayed){
            if (game.getPlayerX().getUsername().equals(player1) && game.getPlayerO().getUsername().equals(player2)
            ||  game.getPlayerO().getUsername().equals(player1) && game.getPlayerX().getUsername().equals(player2)){
                return game;
            }
        }
        return null;
    }

    /**
     * Registers all message handlers and subscribes the server to the "main" pubnub channel.
     */
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
