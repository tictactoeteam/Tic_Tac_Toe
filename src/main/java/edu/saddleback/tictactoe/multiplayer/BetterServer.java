package edu.saddleback.tictactoe.multiplayer;

import edu.saddleback.tictactoe.messages.Request;
import edu.saddleback.tictactoe.messages.Response;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;
import edu.saddleback.tictactoe.view.TicTacToeApplication;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.util.ArrayList;

public class BetterServer {
    private ArrayList<BetterGame> games = new ArrayList<>();
    private ServerSocket serverSocket;
    private Socket connection;
    private static final int PORT = 6969;
    private int currentGameID = 1;

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

                            //Sends game end state message
                            if(game1.getWinner() == GamePiece.X){
                                send(Response.createGameOverResponse(game1.getPlayer1(), game1.getPlayer2()));
                            }else if(game1.getWinner() == GamePiece.O){
                                send(Response.createGameOverResponse(game1.getPlayer2(), game1.getPlayer1()));
                            }else if(game1.isDrawn()){
                                send(Response.createGameOverResponse("DRAW", "DRAW"));
                            }

                            if (game1.isSinglePlayerMode()){
                                game1.makeAMove();

                                //Sends game end state message
                                if(game1.getWinner() == GamePiece.X){
                                    send(Response.createGameOverResponse(game1.getPlayer1(), game1.getPlayer2()));
                                }else if(game1.getWinner() == GamePiece.O){
                                    send(Response.createGameOverResponse(game1.getPlayer2(), game1.getPlayer1()));
                                }else if(game1.isDrawn()){
                                    send(Response.createGameOverResponse("DRAW", "DRAW"));
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
                        BetterGame game2 = new BetterGame(currentGameID);
                        game2.setPlayer1(((String[])request.getData())[0]);
                        game2.setPlayer2(((String[])request.getData())[1]);
                        games.add(game2);
                        send(Response.createGameBeginsResponse(new String[]{"lp1", "lp2"}, game2.getBoard(), game2.getGameId()));
                        currentGameID++;
                        break;

                    case "SinglePlayer":
                        System.out.println(">>>>>LocalSinglePlayer request received!");
                        BetterGame game3 = new BetterGame(currentGameID, true);
                        String playerN = (String)(((Serializable[])request.getData())[0]);
                        boolean mrBillGoesFirst = (boolean)(((Serializable[])request.getData())[1]);
                        if (mrBillGoesFirst){
                            game3.setPlayer1("Mr.Bill");
                            game3.setPlayer2(playerN);
                            game3.makeAMove();
                            games.add(game3);
                            send(Response.createGameBeginsResponse(new String[]{"Mr.Bill", playerN}, game3.getBoard(), game3.getGameId()));
                        }else{
                            game3.setPlayer1(playerN);
                            game3.setPlayer2("Mr.Bill");
                            games.add(game3);
                            send(Response.createGameBeginsResponse(new String[]{playerN, "Mr.Bill"}, game3.getBoard(), game3.getGameId()));
                        }
                        currentGameID++;
                        break;

                    case "Reset":
                        System.out.println(">>>>>Reset request received!");
                        games.remove(searchForGame((int)(Serializable)request.getData()));
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        send(Response.createCloseClientThreadOnResetResponse());
                        throw new InvalidKeyException("JUST MEANS RESET BUTTON WAS CLICKED");

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
        }catch(InvalidKeyException ex) {//This is used to escape the while loop and run this code if reset is clicked.

            System.out.println("============================");

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

    public void closeTheServer(){

        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
