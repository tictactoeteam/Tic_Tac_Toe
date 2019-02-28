package edu.saddleback.tictactoe.multiplayer;

import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.model.Board;

import java.net.Socket;

public class GameThread extends Thread {

    private GameThreadRunnable runnable;


    public GameThread(Socket socketPlayerX, Socket socketPlayerO) {
        runnable = new GameThreadRunnable(socketPlayerX, socketPlayerO);
    }

    @Override
    public synchronized void start() {
        runnable.run();
    }
}



class GameThreadRunnable implements Runnable{
    private Socket socketPlayerX;
    private Socket socketPlayerO;
    private AdvancedEvaluator winnerChecker;

    GameThreadRunnable(Socket socketPlayerX, Socket socketPlayerO){
        this.socketPlayerX = socketPlayerX;
        this.socketPlayerO = socketPlayerO;
        winnerChecker = new AdvancedEvaluator();
    }

    @Override
    public void run() {

        Socket currentPlayer = socketPlayerX;
        Socket nonCurrentPlayer = socketPlayerO;
        Socket temp;

        Board board = new Board();
        while (winnerChecker.evaluate(board) != 0 || board.getTurnNumber() != 9){
            /**board = get board from CurrentPlayer**/
            /**send the board to NonCurrentPlayer**/


            System.out.println(board);
            temp = currentPlayer;
            currentPlayer = nonCurrentPlayer;
            nonCurrentPlayer = temp;
        }

    }
}