package edu.saddleback.tictactoe.multiplayer;

import edu.saddleback.tictactoe.messages.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BetterServer {
    private ServerSocket serverSocket;
    private Socket connection;
    private static final int PORT = 6969;

    public static void main(String[] args){
        BetterServer s = new BetterServer();
        s.fireTheServer();
    }

    private Request receive() throws IOException {
        ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
        try {
            return (Request) input.readObject();
        }
        catch(ClassNotFoundException ex){
            System.out.println("Something went wrong with serialization!");
            return null;
        }
    }

    private Thread serverBehavior = new Thread(() -> {
        System.out.println("Waiting for a connection...");
        try {
            connection = serverSocket.accept();
            System.out.println("Connection established with " + connection.getInetAddress());
            while (true) {
                System.out.println("Waiting for a message to come...");
                Request request = receive();

                switch (request.getType()) {
                    case "Host":
                        System.out.println(">>>>>Host request received!");
                        System.out.println(">>>Player name that requested:" + request.getData());
                        break;
                    case "Join":
                        System.out.println(">>>>>Join request received!");
                        System.out.println(">>>Join code given: " + request.getData()) ;
                        break;
                    case "MoveValidate":
                        System.out.println(">>>>>MoveValidate request received!");
                        System.out.println(">>>Move to validate: " + request.getData());
                        break;
                    default:
                        System.out.println(">>>>>Unknown request received! Type: " + request.getType());
                        break;

                }

                System.out.println("============================");

            }
        }catch(IOException ex){
            System.out.println("Something went wrong with the connection!");
        }catch(NullPointerException ex){
            System.out.println("Null pointer Exception!");
        }
    });



    public BetterServer(){
        this(PORT);
    }
    public BetterServer(int port){
        System.out.println("Creating a server on port no. " + port);
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Successful!");
        }catch(IOException ex){
            System.out.println("Something went wrong!");
        }
    }

    public void fireTheServer(){
        System.out.println("Starting the server!");
        serverBehavior.start();
    }
}
