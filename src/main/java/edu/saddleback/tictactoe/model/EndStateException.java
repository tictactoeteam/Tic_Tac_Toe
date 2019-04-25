package edu.saddleback.tictactoe.model;


public class EndStateException extends Exception {
    private String winner;
    private String loser;

    public EndStateException(){
        this(null, null);
    }

    public EndStateException(String winner, String loser){
        this.winner = winner;
        this.loser = loser;
    }

    public boolean isDrawn(){
        return (winner == null && loser == null);
    }

    public String getWinner(){
        return winner;
    }

    public String getLoser(){
        return loser;
    }
}
