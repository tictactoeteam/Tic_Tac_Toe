package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.decision.Minimax;
import edu.saddleback.tictactoe.decision.Node;
import edu.saddleback.tictactoe.decision.StaticEvaluator;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;

public class ComputerPlayer extends Player{
    private Minimax MrBill;
    private Node root;

    public ComputerPlayer(GameController hope, Node root){
        this(hope, root, "127.0.0.1");
    }

    public ComputerPlayer(GameController hope, Minimax mrBill){
        super(hope);
        this.MrBill = mrBill;
        this.root = mrBill.getTreeRoot();
        behavior = new Thread(() -> {
            while(winnerChecker.evaluate(hope.getBoard()) == 0 || hope.getBoard().getTurnNumber() < 9) {
                readBoard();
                boardMove = null;
                findMove();
                sendMove();
                boardMove = null;
            }
        });

    }

    public ComputerPlayer(GameController hope, Node root, String IP){
        this(hope, root, IP, 6969);
    }

    public ComputerPlayer(GameController hope, Node root, String IP, int port){
        super(hope, false, IP, port);
        this.root = root;
        MrBill = new Minimax(new AdvancedEvaluator(), root);
        behavior = new Thread(() -> {
            while(winnerChecker.evaluate(hope.getBoard()) == 0 || hope.getBoard().getTurnNumber() < 9) {
                readBoard();
                boardMove = null;
                findMove();
                sendMove();
                boardMove = null;
            }
        });
    }

    public void setEvaluator(StaticEvaluator evaluator){
        MrBill.setEvaluator(evaluator);
    }

    public void findMove(){
        Node currentNode = Node.findNode(hope.getBoard(), root);
        Board currentBoard = MrBill.bestMove(currentNode);
        boardMove = BoardMove.fromTwoBoards(currentNode.getBoard(), currentBoard);

    }

    @Override
    public void setMove(int row, int col, GamePiece piece) {}
}
