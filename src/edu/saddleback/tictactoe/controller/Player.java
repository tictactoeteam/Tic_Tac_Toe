package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;

import java.io.Serializable;

public abstract class Player implements Serializable {
    final protected GameController hope;
    protected BoardMove boardMove;
    protected ServerConnection connection;
    protected Thread behavior;
    protected AdvancedEvaluator winnerChecker;

    private boolean isPlayer1;

    public Player(GameController hope) {
        this(hope, true);
    }

    public Player(GameController hope, boolean isPlayer1){
        this(hope, isPlayer1, "127.0.0.1");
    }

    public Player(GameController hope, boolean isPlayer1, String IP){
        this(hope, isPlayer1, IP, 6969);
    }

    public Player(GameController hope, boolean isPlayer1, String IP, int port){
        this.hope = hope;
        this.isPlayer1 = isPlayer1;
        connection = new ServerConnection(IP, port);
        winnerChecker = new AdvancedEvaluator();
    }

    public void readBoard(){
        Board temp = connection.receiveBoard();
        hope.setBoard(temp);
        hope.notifyBoard();
        // notify listeners??
    }

    public void readName() {
        String name = connection.receiveName();
        System.out.println(name);
        if (isPlayer1) {
            hope.setPlayer2Name(name);
        } else {
            hope.setPlayer1Name(name);
        }
    }

    public void sendMove(){
        connection.sendBoardMove(boardMove);
        try {
            boardMove.applyTo(hope.getBoard());
        }catch(GridAlreadyChosenException ex){}
        hope.notifyBoard();
        // notify listeners??
    }

    public void sendName() {
        if (isPlayer1) {
            connection.sendName(hope.getPlayer1Name());
        } else {
            connection.sendName(hope.getPlayer2Name());
        }
    }

    public boolean isPlayer1() {
        return isPlayer1;
    }

    public abstract void setMove(int row, int col, GamePiece piece);

    public void start(){
        behavior.start();
    }

}
