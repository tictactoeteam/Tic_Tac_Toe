package edu.saddleback.tictactoe.multiplayer;

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
        this(PORT);
    }

    public BetterServer(int port){
        {
            try {
                serverSocket = new ServerSocket(port);
            }catch(IOException ex){
                serverSocket = null;
                System.out.println("Unable to create a server!");
            }
            connectionHandlers = Executors.newCachedThreadPool();
            connections = new Vector<>();
            System.out.println("Server ready to start on port " + port);
        }
    }


    // function that adds a client
    public Socket acceptConnection(){
        try {
            Socket client = serverSocket.accept();
            connections.add(client);
            return client;
        }catch(IOException ex){
            System.out.println("Unable to establish the connection!");
            return null;
        }
    }

    // function that handles a message, I think in PN it's together when you add a client, so this should go
    private Response handle(Request request){
        Response response = null;
        switch(request.getType()) {

            default:
                System.out.println("Unknown request: " + request.getType());
                response = new Response("UnknownRequest!");
        }

        return response;
    }

    // I think goes away, PN will do all crazy threading for us
    private Thread serverBehavior = new Thread(() -> {

        while(true) {
            System.out.println(">>>Waiting for a connection...");
            Socket connection = acceptConnection();
            System.out.println(">>>Connection established: " + connection.getInetAddress());
            connectionHandlers.execute(generateConnectionHandler(connection));
        }

    });


    // I think goes awya for the same reason as the above
    private Runnable generateConnectionHandler(Socket connection){
        return new Runnable() {
            @Override
            public void run() {
                boolean running = true;
                Socket localConnection = connection;
                Request localRequest;
                Response localResponse;

                while(running){
                    try {
                        localRequest = BetterServer.receiveMessage(localConnection);
                        localResponse = handle(localRequest);
                        if (localResponse == null) {
                            running = false;
                        } else {
                            BetterServer.sendMessage(localConnection, localResponse);
                        }
                    }catch(IOException ex){
                        System.out.println("Non-graceful shutdown: connection broken!");
                        running = false;
                    }
                }

                System.out.println("Shutting down the connection!");
                closeConnection(localConnection);
            }
        };
    }


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
    public void start(){
        serverBehavior.start();
    }


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
