package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;

import java.io.Serializable;

public abstract class Player implements Serializable {
    final protected Board board;
    protected BoardMove boardMove;
    protected ServerConnection connection;
    protected Thread behavior;
    protected AdvancedEvaluator winnerChecker;

    public Player(Board board){
        this(board, "127.0.0.1");
    }

    public Player(Board board, String IP){
        this(board, IP, 6969);
    }

    public Player(Board board, String IP, int port){
        this.board = board;
        connection = new ServerConnection(IP, port);
        winnerChecker = new AdvancedEvaluator();
    }

    public void readBoard(){
        Board temp = connection.receiveBoard();
        board.set(temp);
        // notify listeners??
    }

    public void sendMove(){
        connection.sendBoardMove(boardMove);
        try {
            boardMove.applyTo(board);
        }catch(GridAlreadyChosenException ex){}
        // notify listeners??
    }

    public abstract void setMove(int row, int col, GamePiece piece);

    public void start(){
        behavior.start();
    }

}
