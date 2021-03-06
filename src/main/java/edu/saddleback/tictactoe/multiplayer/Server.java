package edu.saddleback.tictactoe.multiplayer;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.sauljohnson.mayo.DiffieHellmanKeyGenerator;
import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.decision.Minimax;
import edu.saddleback.tictactoe.decision.Node;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
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

    private HashMap<String, String> users;

    private Minimax mrBill;
    private Node root = new Node();


    /**
     * Constructor
     */
    public Server() {
        users = new HashMap<>();
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

        Node.generateTree(root);

        //Not sure if we want easy mode Mr.Bill always so... he's always hard sorry you cannot defeat him
        this.mrBill = new Minimax(new AdvancedEvaluator(), root);

        System.out.println(mrBill.bestMove(root));

    }

    public void addUser(String uuid, String username){
        users.put(uuid, username);
    }

    public void removeUser(String uuid){
        users.remove(uuid);
    }

    public String findUser(String uuid){
        return users.get(uuid);
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

    public void createMrBillGame(String player1){
        gamesPlayed.add(new Game("Mr Bill", player1));
    }

    public void removeGame(Game game){
        gamesPlayed.remove(game);
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


    public Game findGame(String player){
        for (Game game : gamesPlayed){
            if (game.getPlayerO().getUsername().equals(player) || game.getPlayerX().getUsername().equals(player))
                return game;
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
        this.delegator.addHandler("serverPub", new PeopleLeavingHandler(this));
        this.delegator.addHandler("challengeAccepted", new MrBillGameStartsHandler(this));
        this.delegator.addHandler("moveResp", new MrBillMoveHandler(this));
        this.delegator.addHandler("getAllGames", new GameDaoHandler());
        pubnub.subscribe().channels(Arrays.asList("main")).withPresence().execute();
    }

    public BoardMove MrBillMakesMove(Game game) {

        System.out.println("MRBILL Makes a move!!");
        Board board1 = game.getBoard();
        Board board2 = mrBill.bestMove(Node.findNode(board1, root));

        System.out.println(">>>>> Board Before: " + board1);
        System.out.println(">>>>> Board After: " + board2);

        return BoardMove.fromTwoBoards(board1, board2);
    }
}
