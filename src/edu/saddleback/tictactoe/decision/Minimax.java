package edu.saddleback.tictactoe.decision;

import edu.saddleback.tictactoe.model.Board;

/**
 * This is the brain of the AI, chooses the best move to make to beat the user.
 */
public class Minimax {
    private StaticEvaluator staticEvaluator;
    private Node treeRoot;

    /**
     * Assigns the specific evauator and tree root for this instance of the AI.
     * @param staticEvaluator
     * @param treeRoot
     */
    public Minimax(StaticEvaluator staticEvaluator, Node treeRoot){

        this.staticEvaluator = staticEvaluator;
        this.treeRoot = treeRoot;

    }

    /**
     * Goes through each possible choice in the game decision tree and calculates the best decision score, based
     * on the calculated score from the given static evaluator.
     * @param n
     * @param depth
     * @param max
     * @return the best score(move) possible.
     */
    public int minimax(Node n, int depth, boolean max){

        if(depth == 0 || n.getChildren().length == 0 || n.getChildren()[0] == null){

            return staticEvaluator.evaluate(n.getBoard());

        }

        if(max){

            int bestScore = Integer.MIN_VALUE;
            for(Node child : n.getChildren()){

                int score = minimax(child, depth - 1, !max);
                bestScore = Math.max(bestScore, score);

            }
            return bestScore;

        }else{

            int bestScore = Integer.MAX_VALUE;
            for (Node child : n.getChildren()){

                int score = minimax(child, depth - 1, !max);
                bestScore = Math.min(bestScore, score);

            }
            return bestScore;

        }
    }

    /**
     * Sets the static evaluator to the given evaluator.
     * @param ev
     */
    public void setEvaluator(StaticEvaluator ev){
        this.staticEvaluator = ev;
    }

    /**
     * Decides on the best move to make based on the best score out of each possible move's score, and returns the
     * updated board.
     * @param parent
     * @return Updated board with the AI's best move placed.
     */
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

    public Node getTreeRoot(){
        return treeRoot;
    }
}