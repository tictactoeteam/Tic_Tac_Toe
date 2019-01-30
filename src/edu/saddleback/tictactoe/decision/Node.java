package edu.saddleback.tictactoe.decision;
import edu.saddleback.tictactoe.board.BoardController;

public class Node {
    private BoardController boardController;

    private Node[] children;


    public BoardController getBoardController(){
        return boardController;
    }

    public Node[] getChildren() {
        return children;
    }
}
