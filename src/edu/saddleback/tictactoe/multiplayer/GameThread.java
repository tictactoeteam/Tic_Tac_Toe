package edu.saddleback.tictactoe.multiplayer;

import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.decision.Node;
import edu.saddleback.tictactoe.decision.RandomEvaluator;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;

import java.io.*;
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

        File saveFile = new File(System.getProperty("user.home") + "/.tictactoe_save");
        Board board = new Board();

        if(saveFile.exists()){
            try {

                FileInputStream fileStream = new FileInputStream(System.getProperty("user.home") + "/.tictactoe_save");
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);

               objectStream.readBoolean();
               objectStream.readUTF();
               objectStream.readUTF();
               board = (Board) objectStream.readObject();
            } catch (IOException e){

                System.out.println("Failed to read existing savegame - making a new game");
                saveFile.delete();

            } catch (ClassNotFoundException e){

                System.out.println("Outdated savegame - making an new game");
                saveFile.delete();
            }
        }

        BoardMove boardMove;

        try{
            sending = new ObjectOutputStream(currentPlayer.getOutputStream());
            sending.writeObject(board);
            sending.flush();
            //We need a way to implement a shut down to this thread... this thread needs to be shutdown when we press
            //X
            while ((winnerChecker.evaluate(board) == 0 && board.getTurnNumber() < 9)) {
                reading = new ObjectInputStream(currentPlayer.getInputStream());
                sending = new ObjectOutputStream(nonCurrentPlayer.getOutputStream());


                boardMove = (BoardMove)reading.readObject();

                try{
                    boardMove.applyTo(board);
                    sending.writeObject(board);
                    sending.flush();
                    try {
                        Thread.sleep(50);
                    }catch(InterruptedException ex){}
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

        System.out.println("HEY I MADE IT OUT OF THIS LOOP");

    }
}