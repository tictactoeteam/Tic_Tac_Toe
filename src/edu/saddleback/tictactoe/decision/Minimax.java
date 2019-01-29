package edu.saddleback.tictactoe.decision;

public class Minimax {
    private StaticEvaluator staticEvaluator;

    public Minimax(StaticEvaluator staticEvaluator) {
        this.staticEvaluator = staticEvaluator;
    }

    public int minimax(Node n, int depth, int alpha, int beta, boolean max) {
        if (depth == 0 || n.getChildren().length == 0) {
            return staticEvaluator.evaluate(n);
        }

        if (max) {
            int bestScore = Integer.MIN_VALUE;
            for (Node child : n.getChildren()) {
                int score = minimax(child, depth - 1, alpha, beta, false);
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha)
                    break;
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (Node child : n.getChildren()) {
                int score = minimax(child, depth - 1, alpha, beta, true);
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, score);
                if (beta <= alpha)
                    break;
            }
            return bestScore;
        }
    }
}
