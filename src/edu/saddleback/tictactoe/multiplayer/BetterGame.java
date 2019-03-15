package edu.saddleback.tictactoe.multiplayer;

import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;

public class BetterGame {
    private Board board;
    private final int gameId;
    private String player1;
    private String player2;
    private AdvancedEvaluator winnerChecker;

    public BetterGame(int gameId){
        this.gameId = gameId;
        board = new Board();
        player1 = "X-Player";
        player2 = "O-Player";
        winnerChecker = new AdvancedEvaluator();
    }

    public Board getBoard(){
        return board;
    }

    public int getGameId(){
        return gameId;
    }

    public void setPlayer1(String player1){
        this.player1 = player1;
    }

    public void setPlayer2(String player2){
        this.player2 = player2;
    }

    public String getPlayer1(){
        return player1;
    }

    public String getPlayer2(){
        return player2;
    }

    public void applyMove(BoardMove move)throws GridAlreadyChosenException {
        move.applyTo(board);
    }

    public GamePiece getWinner(){
        int result = winnerChecker.evaluate(board);
        if (result == 0)
            return null;
        if (result >0)
            return GamePiece.X;
        return GamePiece.O;
    }

    public boolean isDrawn(){
        return getWinner()==null && board.getTurnNumber()==9;
    }

}
