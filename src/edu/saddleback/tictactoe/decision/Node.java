package edu.saddleback.tictactoe.decision;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;

public class Node{
    private Board board;
    private Node[] children;

    private static StaticEvaluator defaultEvaluator = new AdvancedEvaluator();

    public Node(){
        this(9);
    }

    public Node(int numberChildren){
        board = new Board();
        children = new Node[numberChildren];
    }
    
    public static void generateTree(Node root){

        // If we hit the winning position or a draw, stop generating (Exit condition)
        if (defaultEvaluator.evaluate(root.getBoard()) != 0 || root.getBoard().getTurnNumber() == 9)
            return;

        // Else, keep generating recursively

        // Step0-Determine the turn player.
        GamePiece turnPlayer;
        if (root.getBoard().isXTurn()) {
            turnPlayer = GamePiece.X;
        }
        else {
            turnPlayer = GamePiece.O;
        }

        // Step1-generate a new board for every child of root
        int parentalBranches = root.getChildren().length;
        int childrenBranches = parentalBranches - 1;
        for (int i=0; i<parentalBranches; ++i){
            root.getChildren()[i] = new Node(childrenBranches);
            root.getChildren()[i].setBoard((Board)(root.getBoard().clone()));
        }

        // Step2-Place the pieces
        int childIndex=0;
        int placement = 0;
        while(childIndex<parentalBranches){
            try {
                root.getChildren()[childIndex].getBoard().set(placement %3, placement /3, turnPlayer);
                childIndex++;
            }catch(GridAlreadyChosenException ex){}
            placement++;
        }

        // Step3-Let the recursive madness begin. Call generateTree() on each child
        for (int i=0; i<parentalBranches; ++i){
            generateTree(root.getChildren()[i]);
        }


    }


    public static Node findNode(Board board, Node root) {

        if (root != null) {
            if (board.getTurnNumber() < root.getBoard().getTurnNumber())
                return null;

            if (board.equals(root.getBoard()))
                return root;

            for (Node child : root.getChildren()) {
                Node temp = Node.findNode(board, child);
                if (temp != null) {
                    return temp;
                }
            }
        }
        return null;

    }

    public Board getBoard(){
        return board;
    }

    public void setBoard(Board board){this.board = board;}

    public Node[] getChildren() {
        return children;
    }
}
