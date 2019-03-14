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

    public ComputerPlayer(Board board, Node root){
        this(board, root, "127.0.0.1");
    }

    public ComputerPlayer(Board board, Minimax mrBill){
        super(new GameController());
        this.MrBill = mrBill;
        this.root = mrBill.getTreeRoot();
        behavior = new Thread(() -> {
            while(winnerChecker.evaluate(board) == 0 || board.getTurnNumber() < 9) {
                readBoard();
                boardMove = null;
                findMove();
                sendMove();
                boardMove = null;
            }
        });

    }

    public ComputerPlayer(Board board, Node root, String IP){
        this(board, root, IP, 6969);
    }

    public ComputerPlayer(Board board, Node root, String IP, int port){
        super(new GameController(), IP, port);
        this.root = root;
        MrBill = new Minimax(new AdvancedEvaluator(), root);
        behavior = new Thread(() -> {
            while(winnerChecker.evaluate(board) == 0 || board.getTurnNumber() < 9) {
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
