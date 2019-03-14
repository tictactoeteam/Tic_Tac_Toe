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

    public Player(GameController hope){
        this(hope, "127.0.0.1");
    }

    public Player(GameController hope, String IP){
        this(hope, IP, 6969);
    }

    public Player(GameController hope, String IP, int port){
        this.hope = hope;
        connection = new ServerConnection(IP, port);
        winnerChecker = new AdvancedEvaluator();
    }

    public void readBoard(){
        Board temp = connection.receiveBoard();
        hope.setBoard(temp);
        hope.notifyListeners();
        // notify listeners??
    }

    public void sendMove(){
        connection.sendBoardMove(boardMove);
        try {
            boardMove.applyTo(hope.getBoard());
        }catch(GridAlreadyChosenException ex){}
        hope.notifyListeners();
        // notify listeners??
    }

    public abstract void setMove(int row, int col, GamePiece piece);

    public void start(){
        behavior.start();
    }

}
