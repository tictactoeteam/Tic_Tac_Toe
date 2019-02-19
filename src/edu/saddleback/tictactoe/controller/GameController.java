package edu.saddleback.tictactoe.controller;

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
import java.util.ArrayList;

/**
 * This class represents a controller for all data and ui functions for the game controller grid pane.
 */
public class GameController {
    private static final String SAVE_LOCATION = System.getProperty("user.home") + "/.tictactoe_save";
    private Board board;
    private ArrayList<BoardUpdatedListener> boardListeners;

    private boolean isMultiplayer;
    private String player1Name, player2Name;

    public GameController() {
        this.boardListeners = new ArrayList<>();

        File saveFile = new File(SAVE_LOCATION);
        if (saveFile.exists()) {
            try {
                FileInputStream fileStream = new FileInputStream(SAVE_LOCATION);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);

                this.board = (Board) objectStream.readObject();
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
        if (board.getTurnNumber() != 0) {
            try {
                FileOutputStream fileStream = new FileOutputStream(SAVE_LOCATION);
                ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
                objectStream.writeObject(board);
                objectStream.close();
            } catch (IOException e) {
                System.out.println("Failed to save game - oh well, exiting");
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the Piece[][] with the current move.
     *
     * @param gridBox
     */
    public void onGridClicked(GridBox gridBox) {
        try {
            GamePiece piece = board.isXTurn() ? GamePiece.X : GamePiece.O;
            board.set(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);
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

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }
}
