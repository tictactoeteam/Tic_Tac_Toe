package edu.saddleback.tictactoe.controller;

import com.google.gson.JsonObject;
import com.sun.nio.sctp.SctpSocketOption;
import edu.saddleback.tictactoe.controller.handlers.MoveHandler;
import edu.saddleback.tictactoe.model.*;
import edu.saddleback.tictactoe.multiplayer.MessageDelegator;
import edu.saddleback.tictactoe.observable.Observable;
import edu.saddleback.tictactoe.view.GridBox;

import java.sql.SQLOutput;
import java.util.UUID;

/**
 * This class represents a controller for all data and ui functions for the game controller grid pane.
 */
public class GameController {

    private Observable<Board> board;

    private String player1Name;
    private String player2Name;
    private UUID playerID = UUID.randomUUID();
    private String winnerName = null;
    private String loserName = null;

    private GamePiece myPiece;

    private MessageDelegator delegator = new MessageDelegator();

    public String getWinnerName(){return winnerName;}
    public String getLoserName(){return loserName;}
    public String getPlayer1Name(){return player1Name;}
    public String getPlayer2Name(){return player2Name;}
    public void setPlayer1Name(String name){player1Name = name;}
    public void setPlayer2Name(String name){player2Name = name;}

    /**
     * Reads game data if a file exists, otherwise initializes a new board.
     */
    public GameController() {
        this.board = new Observable<>();
        delegator.addHandler("moveResp", new MoveHandler());
    }

    /**
     * Saves all game information to a file if the application is terminated, deletes the save file if the game is over.
     */
    public void onExitRequested() {
        System.exit(1);
    }

    /**
     * Updates the board data with the current move, and if it is in single player, the ai makes the specific move.
     *
     * @param gridBox
     */
    public void onGridClicked(GridBox gridBox){
//        GamePiece piece = board.get().isXTurn() ? GamePiece.X : GamePiece.O;
//        BoardMove currentMove = new BoardMove(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);
        ServerConnection.getInstance().sendMessage(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), myPiece);
    }

    public void resetGame() {
        this.board.set(null);
    }

    /**
     * Generates a win message depending on who won the game, and the random message.
     * @param
     * @return
     */
    public String generateWinMessage(){
        String win = getWinnerName();
        String los = getLoserName();

        if(win.equals("DRAW") && los.equals("DRAW"))
            return "DRAW!";

        String[] possibilities= new String[]{
                los + " got spanked by " + win,
                win + " takes it all, " + los + " standing small",
                win + " asserted dominance over " + los,
                win + " kicked the butt of " + los,
                win + " wiped the floor with " + los,
                los + " perished in a battle against " + win,
                los + " was no match for " + win,
                win + ": 1, " + los + ": 0",
                los + ", bend your knee before " + win,
                los + ", you now serve to " + win
        };


        return possibilities[(int)(Math.random()*possibilities.length)];
    }

    public void setBoard(Board board){
        this.board.set(board);
    }


    public void setMyPiece(GamePiece piece){
        myPiece = piece;
    }

    public void applyJsonMove(JsonObject move){
        Board temp = this.board.get();
        try {
            JsonMove.convertToBoardMove(move).applyTo(temp);
            this.board.set(temp);
        }catch(GridAlreadyChosenException ex){
            System.out.println("This shouldn't happen!!!! Validation of the move on the server is faulty!!");
            System.out.println("Or the conversion from Json to BoardMove is faulty-one or the other");
        }
    }
}
