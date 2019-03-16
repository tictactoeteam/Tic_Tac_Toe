package edu.saddleback.tictactoe.multiplayer;

import edu.saddleback.tictactoe.messages.Request;
import edu.saddleback.tictactoe.messages.Response;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BetterServer {
    private ArrayList<BetterGame> games = new ArrayList<>();
    private ServerSocket serverSocket;
    private Socket connection;
    private static final int PORT = 6969;
    private int localGameID = -1;
    private final int currentGameID = 1;

//    public static void main(String[] args){
//        BetterServer s = new BetterServer();
//        s.fireTheServer();
//    }

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

    private void send(Response response) throws IOException{
        ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
        output.writeObject(response);
        output.flush();
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
                        System.out.println(">>>Joining player name: "+ ((Serializable[])request.getData())[0]);
                        System.out.println(">>>Join code given: " + ((Serializable[])request.getData())[1]) ;
                        break;
                    case "MoveValidate":
                        System.out.println(">>>>>MoveValidate request received!");
                        BoardMove move = (BoardMove)(((Serializable[])request.getData())[0]);
                        int ID = (Integer)(((Serializable[])request.getData())[1]);
                        BetterGame game1 = searchForGame(ID);

                        if (game1 == null){
                            send(Response.createMoveInvalidResponse(new Board()));
                            break;
                        }
                        try{
                            game1.applyMove(move);
                            if(game1.getWinner() != null){
                                send(Response.createYouWinResponse("Idk what to do over here"));
                            }
                            if (game1.isSinglePlayerMode()){
                                game1.makeAMove();
                                if (game1.getWinner()!= null){
                                    send(Response.createYouWinResponse("Idk what to do over here"));
                                }
                            }
                            send(Response.createMoveValidResponse(game1.getBoard()));
                        }catch(GridAlreadyChosenException ex){
                            send(Response.createMoveInvalidResponse(game1.getBoard()));
                        }

                        break;
                    case "LocalMultiplayer":
                        System.out.println(">>>>>LocalMultiplayer request received!");
                        System.out.println(">>>Player Names: " + ((String[])request.getData())[0] + " " + ((String[])request.getData())[1]);
                        BetterGame game2 = new BetterGame(localGameID);
                        game2.setPlayer1(((String[])request.getData())[0]);
                        game2.setPlayer2(((String[])request.getData())[1]);
                        games.add(game2);
                        send(Response.createGameBeginsResponse(new String[]{"lp1", "lp2"}, game2.getBoard()));
                        break;
                    case "SinglePlayer":
                        System.out.println(">>>>>LocalSinglePlayer request received!");
                        BetterGame game3 = new BetterGame(localGameID, true);
                        String playerN = (String)(((Serializable[])request.getData())[0]);
                        boolean mrBillGoesFirst = (boolean)(((Serializable[])request.getData())[1]);
                        if (mrBillGoesFirst){
                            game3.setPlayer1("Mr.Bill");
                            game3.setPlayer2(playerN);
                            game3.makeAMove();
                            games.add(game3);
                            send(Response.createGameBeginsResponse(new String[]{"Mr.Bill", playerN}, game3.getBoard()));
                        }else{
                            game3.setPlayer1(playerN);
                            game3.setPlayer2("Mr.Bill");
                            games.add(game3);
                            send(Response.createGameBeginsResponse(new String[]{playerN, "Mr.Bill"}, game3.getBoard()));
                        }
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

    public BetterGame searchForGame(int gameId){
        for(BetterGame g : games){
            if(g.getGameId() == gameId)
                return g;
        }
        return null;
    }

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
