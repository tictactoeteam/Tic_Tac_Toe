package edu.saddleback.tictactoe.multiplayer;

import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameThread extends Thread {

    private GameThreadRunnable runnable;



    public GameThread(Socket socketPlayerX, Socket socketPlayerO) {
        runnable = new GameThreadRunnable(socketPlayerX, socketPlayerO);
    }

    @Override
    public synchronized void start() {
        runnable.run();
        System.out.println("Game Terminated!");
    }


    public void cease(){
        runnable.threadIsGoodAndRunning = false;
    }
}



class GameThreadRunnable implements Runnable{
    private Socket socketPlayerX;
    private Socket socketPlayerO;
    private AdvancedEvaluator winnerChecker;
    boolean threadIsGoodAndRunning = true;

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

        ObjectOutputStream sending;
        ObjectInputStream reading;

        Board board = new Board();
        BoardMove boardMove;

        try{
            sending = new ObjectOutputStream(currentPlayer.getOutputStream());
            sending.writeObject(board);
            sending.flush();
            while ((winnerChecker.evaluate(board) == 0 || board.getTurnNumber() < 9) && threadIsGoodAndRunning) {
                reading = new ObjectInputStream(currentPlayer.getInputStream());
                sending = new ObjectOutputStream(nonCurrentPlayer.getOutputStream());


                boardMove = (BoardMove)reading.readObject();

                try{
                    boardMove.applyTo(board);
                    sending.writeObject(board);
                    sending.flush();

                    System.out.println(board);

                    temp = currentPlayer;
                    currentPlayer = nonCurrentPlayer;
                    nonCurrentPlayer = temp;
                }catch(GridAlreadyChosenException ex){
                    sending = new ObjectOutputStream(currentPlayer.getOutputStream());
                    sending.writeObject(board);
                    sending.flush();
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }



    }
}