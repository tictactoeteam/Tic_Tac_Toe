package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.messages.Request;
import edu.saddleback.tictactoe.messages.Response;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection {

    private Socket connection;

    private ObjectInputStream receiving;
    private ObjectOutputStream sending;

//    public static void main(String[] args){
//        System.out.println("Trying to connect first player!");
//        ServerConnection player1 = new ServerConnection();
//        System.out.println("Connection succesful!");
//        try {
//            Thread.sleep(500);
//        }catch(InterruptedException ex){}
//
//        System.out.println("Tying to connect second player!");
//        ServerConnection player2 = new ServerConnection();
//        System.out.println("Connection succesful!");
//
//        System.out.println("Sending an empty board!");
//        player1.sendBoardMove(new BoardMove(0, 0, GamePiece.X));
//
//        System.out.println("Receiving an empty board!");
//        Board board = player2.receiveBoard();
//
//        try {
//            board.set(0, 0, GamePiece.X);
//        }catch(GridAlreadyChosenException ex){
//            ex.printStackTrace();
//        }
//        System.out.println("Sending something else!");
//        player2.sendBoard(board);
//
//
//
//    }



    /**Default Constructor
     * Default IP address = "127.0.0.1"
     * Default port = 6969
     * **/
    public ServerConnection(){
        this("127.0.0.1");
    }

    /**Constructor that accepts an IP address
     * Default port = 6969
     * **/
    public ServerConnection(String IP){
        this(IP, 6969);
    }

    /**Constructor that accepts an IP address and a port number**/
    public ServerConnection(String IP, int port){
        try {
            connection = new Socket(IP, port);

        } catch(IOException ex){
            System.out.println("Connection failed!");
        }
    }

    public void sendRequest(Request request){
        try {
            sending = new ObjectOutputStream(connection.getOutputStream());
            sending.writeObject(request);
            sending.flush();
        }catch(IOException ex){
            System.out.println("Things went bad!");
        }
    }

    public Response receiveResponse(){
        try{
            receiving = new ObjectInputStream(connection.getInputStream());
            return (Response)receiving.readObject();
        }catch(IOException ex){}
        catch(ClassNotFoundException ex){}
        System.out.println("Something went really bad!");
        return null;
    }


    /**method that sends the board across the connection**/
    void sendBoardMove(BoardMove boardMove){
        try {
            sending = new ObjectOutputStream(connection.getOutputStream());
            sending.writeObject(boardMove);
            sending.flush();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    void sendName(String name) {
        try {
            sending = new ObjectOutputStream(connection.getOutputStream());
            sending.writeObject(name);
            sending.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**method that waits for the board object to be received**/
    Board receiveBoard(){
        try {
            receiving = new ObjectInputStream(connection.getInputStream());
            Board result = (Board)receiving.readObject();
            return result;
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return null;
    }

    String receiveName() {
        try {
            receiving = new ObjectInputStream(connection.getInputStream());
            return (String) receiving.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
