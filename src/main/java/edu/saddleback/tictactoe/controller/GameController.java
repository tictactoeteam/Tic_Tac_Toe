package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.observable.BoardUpdatedListener;
import edu.saddleback.tictactoe.view.GridBox;
import java.util.ArrayList;

/**
 * This class represents a controller for all data and ui functions for the game controller grid pane.
 */
public class GameController {

    private Board board;
    private ArrayList<BoardUpdatedListener> boardListeners;

    private String winnerName = null;
    private String loserName = null;

    public String getWinnerName(){return winnerName;}
    public String getLoserName(){return loserName;}

    /**
     * Reads game data if a file exists, otherwise initializes a new board.
     */
    public GameController() {

        this.boardListeners = new ArrayList<>();
        this.board = new Board();

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

        GamePiece piece = board.isXTurn() ? GamePiece.X : GamePiece.O;
        BoardMove currentMove = new BoardMove(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);

        //if (gameID!= -1)
            //client.sendRequest(Request.createMoveValidateRequest(currentMove, gameID));

    }

    public void resetGame() {

        //client.sendRequest(Request.createResetRequest(gameID));
        this.board = new Board();
        this.notifyBoard();

    }

    /**
     * Adds a new listener to the board.
     * @param listener
     */
    public void addBoardListener(BoardUpdatedListener listener){
        boardListeners.add(listener);
        listener.update(board);
    }

    /**
     * Updates the board for each listener.
     */
    public void notifyBoard() {
        boardListeners.forEach(listener -> listener.update(board));
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

    public Board getBoard(){
        return board;
    }

}
