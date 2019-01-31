package edu.saddleback.tictactoe.decision;
import edu.saddleback.tictactoe.board.Board;

public class Node {
    private Board board;
    private Node[] children;

    public Board getBoard(){
        return board;
    }

    public Node[] getChildren() {
        return children;
    }
}
