package edu.saddleback.tictactoe.controller;

import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.sauljohnson.mayo.DiffieHellmanKeyGenerator;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.multiplayer.MessageDelegator;
import edu.saddleback.tictactoe.observable.Observable;
import edu.saddleback.tictactoe.util.Crypto;
import edu.saddleback.tictactoe.view.LobbyView;
import edu.saddleback.tictactoe.view.TicTacToeApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class handles the client to server connection and interactions. Many server responses to the received messages
 * will interact with this class, which will in turn update the client UI. SINGLETON
 */
public class ServerConnection {

    private static String pubkey = System.getenv("TTT_PUBNUB_PUBLISH");
    private static String subkey = System.getenv("TTT_PUBNUB_SUBSCRIBE");
    private static ServerConnection instance;
    private MessageDelegator delegator;
    private BigInteger dhPrivateKey;
    private BigInteger dhPublicKey;
    private String attemptedUsername = "noName";
    private Observable<BigInteger> sharedSecret;
    private PubNub pubnub;
    private HashMap<String, String> usernames;
    private boolean visibleInLobby;
    private Observable<Boolean> loggedIn;
    private ObservableList<String> myUserList;
    private Observable<Boolean> gameStart;
    private PNConfiguration pnConfig;

    /**
     * Constructor: builds the secure connection, establishes listeners for message responses from the server.
     */
    private ServerConnection() {

        this.pnConfig = new PNConfiguration();
        pnConfig.setPublishKey(pubkey);
        pnConfig.setSubscribeKey(subkey);

        myUserList = FXCollections.observableArrayList();
        usernames = new HashMap<>();
        visibleInLobby = false;

        this.pubnub = new PubNub(pnConfig);

        this.dhPrivateKey = DiffieHellmanKeyGenerator.generatePrivateKey();
        this.dhPublicKey = DiffieHellmanKeyGenerator.generatePublicKey(this.dhPrivateKey);

        sharedSecret = new Observable<>();
        loggedIn = new Observable<>();
        gameStart = new Observable<>();

        this.delegator = new MessageDelegator();

        waitForServerPub();
        connect();
        registerLoginStatusListener();
        registerChallengeListener();
        registerHashMapFiller();
        registerUserUpdater();
    }

    /**
     * Listener for login status
     */
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

                gimmeUsers();

                String username = data.get("username").getAsString();

                if (type.equals("loggedIn") && username.equals(attemptedUsername)) {
                    loggedIn.set(true);
                    visibleInLobby = true;
                    gimmeUsers();
                }

                if (type.equals("accountCreated") && username.equals(attemptedUsername)) {
                    loggedIn.set(true);
                    visibleInLobby = true;
                    gimmeUsers();
                }

                if (type.equals("badLogin") && username.equals(attemptedUsername)) {
                    loggedIn.set(false);
                    visibleInLobby = false;
                    attemptedUsername = "noName";
                }

                if (type.equals("accountExists") && username.equals(attemptedUsername)) {
                    loggedIn.set(false);
                    visibleInLobby = false;
                    attemptedUsername = "noName";
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {}
        });
    }

    /**
     * Listener for challenge status
     */
    private void registerChallengeListener(){
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {}
            @Override
            public void message(PubNub pubnub, PNMessageResult message) {

                String type = message.getMessage().getAsJsonObject().get("type").getAsString();
                JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();

                if (!Arrays.asList("challengeAccepted", "challengeDenied").contains(type)) {
                    return;
                }

                String player1Name = data.get("player1Username").getAsString();
                String player2Name = data.get("player2Username").getAsString();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(type.equals("challengeAccepted") && attemptedUsername.equals(player1Name)){ //YOU ARE THE 'X'
                            TicTacToeApplication.getController().setPlayer1Name(player1Name);
                            TicTacToeApplication.getController().setPlayer2Name(player2Name);

                            TicTacToeApplication.getController().setMyPiece(GamePiece.X);

                            visibleInLobby = false;

                            gameStart.set(true);

                        }

                        if(type.equals("challengeAccepted") && attemptedUsername.equals(player2Name)){ //YOU ARE THE 'O'
                            TicTacToeApplication.getController().setPlayer1Name(player2Name);
                            TicTacToeApplication.getController().setPlayer2Name(player1Name);

                            TicTacToeApplication.getController().setMyPiece(GamePiece.O);

                            visibleInLobby = false;

                            gameStart.set(true);

                        }

                        if (type.equals("challengeAccepted") && !attemptedUsername.equals(player2Name) && !attemptedUsername.equals(player1Name)){
                            gimmeUsers();
                        }

                    }
                });

            }
            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence){}
        });

    }

    /**
     * Sends the "connect" message
     */
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

    /**
     * Handles outputting the PubNub presence info to the console.
     */
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

                gimmeUsers();
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
                hereNow();
            }
        });

        this.pubnub.subscribe().channels(Arrays.asList("main")).withPresence().execute();

    }

    /**
     * Sends the "login" message with the encrypted username and password.
     * @param username
     * @param password
     */
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

    /**
     * Sends the "signup" message with the encrypted username and password.
     * @param username
     * @param password
     */
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

    /**
     * Sends a message to the server that two players want to start a game.
     * @param player1Name
     * @param player2Name
     */
    public void challenge(String player1Name, String player2Name){

        JsonObject msg = new JsonObject();
        msg.addProperty("type", "challenge");

        JsonObject data = new JsonObject();
        data.addProperty("player1Username", Crypto.encrypt(player1Name, sharedSecret.get()));
        data.addProperty("player2Username", Crypto.encrypt(player2Name, sharedSecret.get()));
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

    /**
     * Sends a message to the server to retrieve all stored games.
     */
    public void getAllGamesMessage(){

        JsonObject msg = new JsonObject();
        msg.addProperty("type", "getAllGames");
        JsonObject data = new JsonObject();
        data.addProperty("test", "CRAP");
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

    /**
     * Returns loggedIn Observable boolean.
     * @return
     */
    public Observable<Boolean> getLoggedInObservable() {
        return loggedIn;
    }

    /**
     * Returns the gameStart Observable boolean.
     * @return
     */
    public Observable<Boolean> getGameStartObservable() {
        return gameStart;
    }

    /**
     * Returns an instance of the ServerConnection.
     * @return
     */
    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }

        return instance;
    }

    /**
     * Returns the myUserList ObservableList
     * @return
     */
    public ObservableList<String> getObservableList(){
        return myUserList;
    }

    /**
     * Sends the move message with the players' usernames and the move object.
     * @param row
     * @param col
     * @param piece
     * @param player1
     * @param player2
     */
    public void sendMessage(int row, int col, GamePiece piece, String player1, String player2){
//        JsonObject msg = JsonMove.convertToJson(row, col,);

        JsonObject msg = new JsonObject();
        msg.addProperty("type", "move");

        JsonObject data = new JsonObject();
        data.addProperty("position", row*3+col);

        String p = "X";
        if (piece== GamePiece.O){
            p = "O";
        }

        System.out.println("PIECE USED: "+ p);
        data.addProperty("piece", p);
        data.addProperty("player1", player1);
        data.addProperty("player2", player2);

        msg.add("data", data);

        try{
            this.pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        }catch(PubNubException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the pubnub object.
     * @return
     */
    public PubNub getPubNub(){return pubnub;}

    /**
     * Runs whenever someone joins or leaves the "main" pubnub channel.
     */
    public void hereNow(){
        myUserList.clear();
        this.pubnub.hereNow()
                .channels(Arrays.asList("main"))
                .includeUUIDs(true)
                .async(new PNCallback<PNHereNowResult>() {
                    @Override
                    public void onResponse(PNHereNowResult result, PNStatus status) {
                        if (status.isError()) {
                            // handle error
                            return;
                        }
                        for (PNHereNowChannelData channelData : result.getChannels().values()) {
                            System.out.println("---");
                            System.out.println("channel:" + channelData.getChannelName());
                            System.out.println("occupancy: " + channelData.getOccupancy());
                            System.out.println("occupants:");
                            for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                                System.out.println("uuid: " + occupant.getUuid() + " state: " + occupant.getState());
                                myUserList.add(occupant.getUuid()); //this adds an element to the list
                            }
                        }

                        LobbyView.updateInstance();

                    }
                });

    }

    /**
     * Adds a username and its UUID to the LobbyView table data structure.
     * @param uuid
     * @param username
     */
    public synchronized void addUser(String uuid, String username){

        System.out.println("PUTTING A USER IN A MAP: ");
        System.out.println("UUID: " + uuid);
        System.out.println("USERNAME: " + username);
        usernames.put(uuid, username);
    }

//    public synchronized void removeUser(String uuid){
//        usernames.remove(uuid);
//    }

    /**
     * Returns the username associated with the given UUID.
     * @param uuid
     * @return
     */
    public synchronized String getUsername(String uuid){
        System.out.println("RETREIVED USESNAME: " + usernames.get(uuid));
        return usernames.get(uuid);
    }

    /**
     * Sends a message to all relevant users to send their usernames for the LobbyView table.
     */
    public void gimmeUsers(){
        usernames = new HashMap<>();
        JsonObject msg = new JsonObject();
        msg.addProperty("type", "gimmeUsers");

        JsonObject data = new JsonObject();
        data.addProperty("requester", attemptedUsername);

        msg.add("data", data);

        System.out.println("REQUESTER (ME): " + attemptedUsername);

        try {
            pubnub.publish()
                    .channel("main")
                    .message(msg)
                    .sync();
        }catch(PubNubException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Handles receiving a "takeMyUsername" message that will update the client's LobbyView table data structure.
     */
    public void registerHashMapFiller(){
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                String type = message.getMessage().getAsJsonObject().get("type").getAsString();
                JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();

                if (!type.equals("takeMyUsername")){
                    return;
                }


                if (!visibleInLobby){
                    return;
                }

                if (attemptedUsername.equals("noName")){
                    return;
                }

                String responderUsername = data.get("username").getAsString();
                String responderUUID = message.getPublisher();
                String requester = data.get("requester").getAsString();

                if (!requester.equals(attemptedUsername)){
                    return;
                }

                if (responderUsername.equals("noName")){
                    return;
                }

                addUser(responderUUID, responderUsername);
                LobbyView.updateInstance();
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
    }

    /**
     * Handles receiving the "gimmeUsers" message that will send this client's username to the requesting user.
     */
    public void registerUserUpdater(){
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                String type = message.getMessage().getAsJsonObject().get("type").getAsString();
                JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();

                if (!type.equals("gimmeUsers")){
                    return;
                }

                if (!visibleInLobby){
                    return;
                }


                if (attemptedUsername.equals("noName")){
                    return;
                }

                String requester = data.get("requester").getAsString();

                if (requester.equals(attemptedUsername)){
                    return;
                }

                if (requester.equals("noName")){
                    return;
                }


                JsonObject msg = new JsonObject();
                msg.addProperty("type", "takeMyUsername");


                JsonObject dt = new JsonObject();
                dt.addProperty("username", attemptedUsername);
                dt.addProperty("requester", requester);
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

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
    }

    /**
     * Returns the attempted username.
     * @return
     */
    public String getAttemptedUsername(){
        return attemptedUsername;
    }

}