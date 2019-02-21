package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.MainApplication;
import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.decision.Minimax;
import edu.saddleback.tictactoe.decision.Node;
import edu.saddleback.tictactoe.decision.RandomEvaluator;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;
import edu.saddleback.tictactoe.model.GridAlreadyChosenException;
import edu.saddleback.tictactoe.observable.BoardUpdatedListener;
import edu.saddleback.tictactoe.view.GridBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketOption;
import java.util.ArrayList;

/**
 * This class represents a controller for all data and ui functions for the game controller grid pane.
 */
public class GameController {
    private static final String SAVE_LOCATION = System.getProperty("user.home") + "/.tictactoe_save";
    private Board board;
    private ArrayList<BoardUpdatedListener> boardListeners;

    private Minimax MrBill;
    private boolean gameDifficulty;
    private Node root;
    private AdvancedEvaluator winnerChecker = new AdvancedEvaluator();

    private boolean isMultiplayer;
    private String player1Name, player2Name;

    public GameController() {
        this.boardListeners = new ArrayList<>();

        File saveFile = new File(SAVE_LOCATION);
        if (saveFile.exists()) {
            try {
                FileInputStream fileStream = new FileInputStream(SAVE_LOCATION);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);

                isMultiplayer = objectStream.readBoolean();
                player1Name = objectStream.readUTF();
                player2Name = objectStream.readUTF();
                this.board = (Board) objectStream.readObject();
                gameDifficulty = objectStream.readBoolean();
                if (!isMultiplayer){
                    Node temp = new Node();
                    Node.generateTree(temp);
                    awakenMrBill(temp);
                    if (gameDifficulty){
                        MrBill.setEvaluator(new AdvancedEvaluator());
                    }
                    else{
                        MrBill.setEvaluator(new RandomEvaluator());
                    }

                }
            } catch (IOException e) {
                System.out.println("Failed to read existing savegame - making a new game");
                saveFile.delete();
            } catch (ClassNotFoundException e) {
                System.out.println("Outdated savegame - making an new game");
                saveFile.delete();
            }
        }

        // if unable to load a game (because no saved game or error), start a new game
        if (this.board == null) {
            this.board = new Board();
        }
    }

    public void onExitRequested() {
        if (board.getTurnNumber() != 0 && checkWinner() == null && !checkDraw()) {
            try {
                FileOutputStream fileStream = new FileOutputStream(SAVE_LOCATION);
                ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
                objectStream.writeBoolean(isMultiplayer);
                objectStream.writeUTF(player1Name);
                objectStream.writeUTF(player2Name);
                objectStream.writeObject(board);
                objectStream.writeBoolean(gameDifficulty);

                objectStream.close();
            } catch (IOException e) {
                System.out.println("Failed to save game - oh well, exiting");
                e.printStackTrace();
            }
        }
        else{
            deleteSaveFile();
        }
    }

    /**
     * Updates the Piece[][] with the current move.
     *
     * @param gridBox
     */
    public void onGridClicked(GridBox gridBox) throws Exception {
        if (checkWinner() != null)
            return;
        if (checkDraw())
            return;

        try {
            GamePiece piece = board.isXTurn() ? GamePiece.X : GamePiece.O;
            board.set(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);
            if(checkWinner() == GamePiece.X){
                System.out.println(generateWinMessage(GamePiece.X));
                notifyListeners();
                MainApplication.getCoordinator().showWinnerScene();
                return;
            }
            if(checkWinner() == GamePiece.O){
                System.out.println(generateWinMessage(GamePiece.O));
                notifyListeners();
                MainApplication.getCoordinator().showWinnerScene();
                return;
            }

            if (checkDraw()){
                System.out.println("DRAW!");
                notifyListeners();
                MainApplication.getCoordinator().showWinnerScene();
                return;
            }
            if(!isMultiplayer()){
                Node temp = Node.findNode(board, root);
                board = MrBill.bestMove(temp);
                if(checkWinner() == GamePiece.X){
                    System.out.println(generateWinMessage(GamePiece.X));
                    notifyListeners();
                    MainApplication.getCoordinator().showWinnerScene();
                    return;
                }
                if(checkWinner() == GamePiece.O){
                    System.out.println(generateWinMessage(GamePiece.O));
                    notifyListeners();
                    MainApplication.getCoordinator().showWinnerScene();
                    return;
                }
                if (checkDraw()){
                    System.out.println("DRAW!");
                    notifyListeners();
                    MainApplication.getCoordinator().showWinnerScene();
                    return;
                }
            }
            notifyListeners();
        } catch (GridAlreadyChosenException e) {
            // TODO: re-prompt for another position
            System.out.println(e.getMessage());
        }
    }

    public void addBoardListener(BoardUpdatedListener listener) {
        boardListeners.add(listener);
        listener.update(board);
    }

    public void deleteSaveFile(){

        File saveFile = new File(SAVE_LOCATION);
        saveFile.delete();
    }

    public boolean gameStateExists(){

        File saveFile = new File(SAVE_LOCATION);
        if(saveFile.exists())
            return true;
        else
            return false;
    }

//    public GamePiece checkWinner(Board board){
//        if (winnerChecker.evaluate(board) > 0)
//            return GamePiece.X;
//        if (winnerChecker.evaluate(board) < 0)
//            return GamePiece.O;
//
//        return null;
//    }

    public GamePiece checkWinner(){
        if (winnerChecker.evaluate(board) > 0)
            return GamePiece.X;
        if (winnerChecker.evaluate(board) < 0)
            return GamePiece.O;

        return null;
    }

    public boolean checkDraw(){
        if (board.getTurnNumber() == 9 && winnerChecker.evaluate(board) == 0)
            return true;
        return false;
    }

    public void removeBoardListener(BoardUpdatedListener listener) {
        boardListeners.remove(listener);
    }

    private void notifyListeners() {
        boardListeners.forEach(listener -> listener.update(board));
    }

    public void setMultiplayer(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public void setDifficulty(String difficulty) {
        if (difficulty.equals("Easy Mode")) {
            MrBill.setEvaluator(new RandomEvaluator());
            gameDifficulty = false;
            System.out.println("Now on easy mode");
        }
        else {
            MrBill.setEvaluator(new AdvancedEvaluator());
            gameDifficulty = true;
            System.out.println("Now on hard mode");
        }
    }

    public void awakenMrBill(Node root){
        this.root = root;
        Node.generateTree(this.root);
        this.MrBill = new Minimax(new AdvancedEvaluator(), this.root);
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public String generateWinMessage(GamePiece winner){
        String win;
        String los;

        if (winner == GamePiece.X){
            win = getPlayer1Name();
            los = getPlayer2Name();
        }
        else if (winner == GamePiece.O){
            win = getPlayer2Name();
            los = getPlayer1Name();
        }
        else{
            return "DRAW!";
        }

        String[] possibilities= new String[]{
                los + " got spanked by " + win,
                win + " takes it all, " + los + " standing small",
                win + " asserted dominance over " + los,
                win + " kicked the butt of " + los,
                win + " wiped the floor with " + los,
                los + " perished in a battle against " + win,
                los + " was no match for " + win,
                win + ": 1, " + los + ": 0",
                los + ", bend your knee before " + win,
                los + ", you now serve to " + win
        };


        return possibilities[(int)(Math.random()*possibilities.length)];
    }

    public void MakeAMove() {
        board = MrBill.bestMove(root);
    }
}
