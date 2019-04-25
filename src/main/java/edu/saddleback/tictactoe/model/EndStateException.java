package edu.saddleback.tictactoe.model;


public class EndStateException extends Exception {
    private String winner;
    private String loser;
    private boolean isDraw;

    public EndStateException(String winner, String loser, boolean isDraww){
        this.winner = winner;
        this.loser = loser;
        this.isDraw = isDraww;
    }

    public boolean isDrawn(){
        return isDraw;
    }

    public String getWinner(){
        return winner;
    }

    public String getLoser(){
        return loser;
    }
}
