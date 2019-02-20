package edu.saddleback.tictactoe.decision;

import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;

public class Minimax {
    private StaticEvaluator staticEvaluator;
    private Node treeRoot;

    public Minimax(StaticEvaluator staticEvaluator, Node treeRoot) {
        this.staticEvaluator = staticEvaluator;
        this.treeRoot = treeRoot;
    }

    public int minimax(Node n, int depth, boolean max) {

        if (depth == 0 || n.getChildren().length == 0 || n.getChildren()[0] == null) {
            return staticEvaluator.evaluate(n.getBoard());
        }

        if (max) {
            int bestScore = Integer.MIN_VALUE;
            for (Node child : n.getChildren()) {
                int score = minimax(child, depth - 1, !max);
                bestScore = Math.max(bestScore, score);
//                alpha = Math.max(alpha, score);
//                if (beta <= alpha)
//                    break;
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (Node child : n.getChildren()) {
                int score = minimax(child, depth - 1, !max);
                bestScore = Math.min(bestScore, score);
//                beta = Math.min(beta, score);
//                if (beta <= alpha)
//                    break;
            }
            return bestScore;
        }
    }


    public void setEvaluator(StaticEvaluator ev){
        this.staticEvaluator = ev;
    }


    public Board bestMove(Node parent){
        int bestScore;
        int bestIndex = 0;
        int temp;

        if (parent.getBoard().isXTurn()){
           bestScore = Integer.MIN_VALUE;

           for (int i=0; i<parent.getChildren().length; ++i){
               temp = (minimax(parent.getChildren()[i], 10, false));
               if(temp > bestScore) {
                   bestIndex = i;
                   bestScore = temp;
               }
           }
        }
        else{
            bestScore = Integer.MAX_VALUE;

            for (int i=0; i<parent.getChildren().length; ++i){
                temp = (minimax(parent.getChildren()[i], 10, true));
                if(temp < bestScore) {
                    bestIndex = i;
                    bestScore = temp;
                }
            }
        }

        return (Board)(parent.getChildren()[bestIndex].getBoard().clone());
    }
}
