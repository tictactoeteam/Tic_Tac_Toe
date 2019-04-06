package edu.saddleback.tictactoe.multiplayer;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import edu.saddleback.tictactoe.messages.Request;
import edu.saddleback.tictactoe.messages.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BetterServer {

    //PN STUFF!!!
    PubNub pubNub;
    PNConfiguration pnConfiguration;



    //stays I think
    private Vector<BetterGame> games = new Vector<>();
    //stays I think, just not as a vector of *sockets*
    private Vector<Socket> connections;
    //goes away I think
    private Executor connectionHandlers;

    //goes away!
    private ServerSocket serverSocket;
    private static final int PORT = 6969;
    private int currentGameID = 1;


    // modifications needed-those boys will configure a PN and stuff
    public BetterServer(){
        this("PublishKey",  "SubscriberKey");
    }

    public BetterServer(String pubKey, String subKey){
        pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSubscribeKey(subKey);
        pnConfiguration.setSecure(false);

        pubNub = new PubNub(pnConfiguration);
    }

//    public BetterServer(int port){
//        {
//            try {
//                serverSocket = new ServerSocket(port);
//            }catch(IOException ex){
//                serverSocket = null;
//                System.out.println("Unable to create a server!");
//            }
//            connectionHandlers = Executors.newCachedThreadPool();
//            connections = new Vector<>();
//            System.out.println("Server ready to start on port " + port);
//        }
//    }


    // function that adds a client
    public void acceptConnection(){
        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getOperation() != null) {
                    switch (status.getOperation()) {
                        // let's combine unsubscribe and subscribe handling for ease of use
                        case PNSubscribeOperation:
                        case PNUnsubscribeOperation:
                            // note: subscribe statuses never have traditional
                            // errors, they just have categories to represent the
                            // different issues or successes that occur as part of subscribe
                            switch (status.getCategory()) {
                                case PNConnectedCategory:
                                    // this is expected for a subscribe, this means there is no error or issue whatsoever
                                case PNReconnectedCategory:
                                    // this usually occurs if subscribe temporarily fails but reconnects. This means
                                    // there was an error but there is no longer any issue
                                case PNDisconnectedCategory:
                                    // this is the expected category for an unsubscribe. This means there
                                    // was no error in unsubscribing from everything
                                case PNUnexpectedDisconnectCategory:
                                    // this is usually an issue with the internet connection, this is an error, handle appropriately
                                case PNAccessDeniedCategory:
                                    // this means that PAM does allow this client to subscribe to this
                                    // channel and channel group configuration. This is another explicit error
                                default:
                                    // More errors can be directly specified by creating explicit cases for other
                                    // error categories of `PNStatusCategory` such as `PNTimeoutCategory` or `PNMalformedFilterExpressionCategory` or `PNDecryptionErrorCategory`
                            }

                        case PNHeartbeatOperation:
                            // heartbeat operations can in fact have errors, so it is important to check first for an error.
                            // For more information on how to configure heartbeat notifications through the status
                            // PNObjectEventListener callback, consult <link to the PNCONFIGURATION heartbeart config>
                            if (status.isError()) {
                                // There was an error with the heartbeat operation, handle here
                            } else {
                                // heartbeat operation was successful
                            }
                        default: {
                            // Encountered unknown status type
                        }
                    }
                } else {
                    // After a reconnection see status.getCategory()
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                String messagePublisher = message.getPublisher();
                System.out.println("Message publisher: " + messagePublisher);
                System.out.println("Message Payload: " + message.getMessage());
                System.out.println("Message Subscription: " + message.getSubscription());
                System.out.println("Message Channel: " + message.getChannel());
                System.out.println("Message timetoken: " + message.getTimetoken());
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

//        try {
//            Socket client = serverSocket.accept();
//            connections.add(client);
//            return client;
//        }catch(IOException ex){
//            System.out.println("Unable to establish the connection!");
//            return null;
//        }
    }

    // function that handles a message, I think in PN it's together when you add a client, so this should go
//    private Response handle(Request request){
//        Response response = null;
//        switch(request.getType()) {
//
//            default:
//                System.out.println("Unknown request: " + request.getType());
//                response = new Response("UnknownRequest!");
//        }
//
//        return response;
//    }

    // I think goes away, PN will do all crazy threading for us
//    private Thread serverBehavior = new Thread(() -> {
//
//        while(true) {
//            System.out.println(">>>Waiting for a connection...");
//            acceptConnection();
//            System.out.println(">>>Connection established");
//            //connectionHandlers.execute(generateConnectionHandler(connection));
//        }
//
//    });


    // I think goes awya for the same reason as the above
//    private Runnable generateConnectionHandler(Socket connection){
//        return new Runnable() {
//            @Override
//            public void run() {
//                boolean running = true;
//                Socket localConnection = connection;
//                Request localRequest;
//                Response localResponse;
//
//                while(running){
//                    try {
//                        localRequest = BetterServer.receiveMessage(localConnection);
//                        localResponse = handle(localRequest);
//                        if (localResponse == null) {
//                            running = false;
//                        } else {
//                            BetterServer.sendMessage(localConnection, localResponse);
//                        }
//                    }catch(IOException ex){
//                        System.out.println("Non-graceful shutdown: connection broken!");
//                        running = false;
//                    }
//                }
//
//                System.out.println("Shutting down the connection!");
//                closeConnection(localConnection);
//            }
//        };
//    }


    // stays, modifications needed
    public static Request receiveMessage(Socket connection) throws IOException{
        try {
            ObjectInputStream read = new ObjectInputStream(connection.getInputStream());

            return (Request) read.readObject();
        }
        catch(ClassNotFoundException ex){
            System.out.println("Casting error while receiving the message!");
            return null;
        }
    }


    // stays, modifications needed
    public static void sendMessage(Socket connection, Response message) throws IOException{

        ObjectOutputStream send = new ObjectOutputStream(connection.getOutputStream());
        send.writeObject(message);
        send.flush();

    }


    // I think goes away
//    public void start(){
//        serverBehavior.start();
//    }


    // unsubscribe method
    public void closeConnection(Socket connection){
        try {
            connections.remove(connection);
            connection.close();
        }catch(IOException ex){
            System.out.println("This shouldn't happen!");
        }
    }


}
