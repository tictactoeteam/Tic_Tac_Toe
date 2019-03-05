package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;
import edu.saddleback.tictactoe.multiplayer.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection {

    Socket connection;

    ObjectInputStream receiving;
    ObjectOutputStream sending;

    public static void main(String[] args){
        System.out.println("Trying to connect first player!");
        ServerConnection player1 = new ServerConnection();
        System.out.println("Connection succesful!");
        try {
            Thread.sleep(500);
        }catch(InterruptedException ex){}

        System.out.println("Tying to connect second player!");
        ServerConnection player2 = new ServerConnection();
        System.out.println("Connection succesful!");

        System.out.println("Sending an empty board!");
        player1.sendBoard(new Board());

        System.out.println("Receiving an empty board!");
        Board board = player2.receiveBoard();

        try {
            board.set(0, 0, GamePiece.X);
        }catch(GridAlreadyChosenException ex){
            ex.printStackTrace();
        }
        System.out.println("Sending something else!");
        player2.sendBoard(new Board());



    }



    /**Default Constructor
     * Default IP address = "127.0.0.1"
     * Default port = 6969
     * **/
    ServerConnection(){
        this("127.0.0.1");
    }

    /**Constructor that accepts an IP address
     * Default port = 6969
     * **/
    ServerConnection(String IP){
        this(IP, 6969);
    }

    /**Constructor that accepts an IP address and a port number**/
    ServerConnection(String IP, int port){
        try {
            connection = new Socket(IP, port);

        } catch(IOException ex){
            ex.printStackTrace();
        }
    }


    /**method that sends the board across the connection**/
    void sendBoard(Board board){
        try {
            sending = new ObjectOutputStream(connection.getOutputStream());
            sending.writeObject(board);
            sending.flush();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**method that waits for the board object to be received**/
    Board receiveBoard(){
        try {
            receiving = new ObjectInputStream(connection.getInputStream());
            Board result = (Board) receiving.readObject();
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



}
