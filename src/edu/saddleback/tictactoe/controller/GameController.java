package edu.saddleback.tictactoe.controller;

import edu.saddleback.tictactoe.messages.Request;
import edu.saddleback.tictactoe.messages.Response;
import edu.saddleback.tictactoe.model.BoardMove;
import edu.saddleback.tictactoe.multiplayer.BetterServer;
import edu.saddleback.tictactoe.multiplayer.Server;
import edu.saddleback.tictactoe.observable.NameUpdatedListener;
import edu.saddleback.tictactoe.view.TicTacToeApplication;
import edu.saddleback.tictactoe.decision.AdvancedEvaluator;
import edu.saddleback.tictactoe.decision.Minimax;
import edu.saddleback.tictactoe.decision.Node;
import edu.saddleback.tictactoe.decision.RandomEvaluator;
import edu.saddleback.tictactoe.model.Board;
import edu.saddleback.tictactoe.model.GamePiece;
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
    private ArrayList<NameUpdatedListener> nameListeners;

    private Minimax MrBill;
    private boolean gameDifficulty;
    private Node root;
    private AdvancedEvaluator winnerChecker = new AdvancedEvaluator();

    private boolean isMultiplayer;
    private String player1Name, player2Name;

    private ServerConnection client;

//    private Player player1;
//    private Player player2;

    private BetterServer localServer;
    private String gameIP;

    /**
     * Reads game data if a file exists, otherwise initializes a new board.
     */
    public GameController() {
        this.boardListeners = new ArrayList<>();
        this.nameListeners = new ArrayList<>();

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

                    if (gameDifficulty)
                        MrBill.setEvaluator(new AdvancedEvaluator());
                    else
                        MrBill.setEvaluator(new RandomEvaluator());

                    //setSinglePlayerUp(false);

                }else{
                    //setLocalMultiplayerUp();
                }

            } catch (IOException e){

                System.out.println("Failed to read existing savegame - making a new game");
                saveFile.delete();

            } catch (ClassNotFoundException e){

                System.out.println("Outdated savegame - making an new game");
                saveFile.delete();

            }
        }



        // if unable to load a game (because no saved game or error), start a new game
        if (this.board == null)
            this.board = new Board();
    }

    /**
     * Saves all game information to a file if the application is terminated, deletes the save file if the game is over.
     */
    public void onExitRequested() {
        if (board.getTurnNumber() != 0 && checkWinner() == null && !checkDraw()) {
            try {

                FileOutputStream fileStream = new FileOutputStream(SAVE_LOCATION);
                System.out.println(SAVE_LOCATION);
                ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
                objectStream.writeBoolean(isMultiplayer);
                objectStream.writeUTF(player1Name);
                objectStream.writeUTF(player2Name);
                objectStream.writeObject(board);
                System.out.println("this is our board we're saving: ");
                System.out.println(board);
                objectStream.writeBoolean(gameDifficulty);
                objectStream.close();

                System.out.println("I am storing everything");



            } catch (IOException e) {

                System.out.println("Failed to save game - oh well, exiting");
                e.printStackTrace();

            }
        }
        else{
            deleteSaveFile();
            System.out.println("I am deleting the game");
        }

        System.exit(1);
    }

    /**
     * Updates the board data with the current move, and if it is in single player, the ai makes the specific move.
     *
     * @param gridBox
     */
    public void onGridClicked(GridBox gridBox){

        GamePiece piece = board.isXTurn() ? GamePiece.X : GamePiece.O;
        String turnPlayer = piece == GamePiece.X ? player1Name : player2Name;
        BoardMove currentMove = new BoardMove(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);

        client.sendRequest(Request.createMoveValidateRequest(currentMove, -1));

//        notifyBoard();
//        if (checkWinner() != null || checkDraw()) {
//            TicTacToeApplication.getCoordinator().showWinnerScene();
//            return;
//        }
////        try {
//
//        GamePiece piece = board.isXTurn() ? GamePiece.X : GamePiece.O;
//
//        try {
//            if (piece == GamePiece.X) {
//                player1.setMove(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);
//                if (player1 instanceof ComputerPlayer) {
//                    player2.setMove(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);
//                }
//            } else {
//                player2.setMove(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);
//                if (player2 instanceof ComputerPlayer) {
//                    player1.setMove(gridBox.getGridRowIndex(), gridBox.getGridColumnIndex(), piece);
//                }
//            }
//        }catch(NullPointerException ex){
//            System.out.println("Wait for other player!!");
//        }
//
//        Thread.sleep(500);
//
//        notifyBoard();
    }

    public void resetGame() {
        this.board = new Board();
        this.notifyBoard();

        this.deleteSaveFile();
    }

    /**
     * Adds a new listener to the board.
     * @param listener
     */
    public void addBoardListener(BoardUpdatedListener listener){
        boardListeners.add(listener);
        listener.update(board);
    }

    public void addNameListener(NameUpdatedListener listener) {
        nameListeners.add(listener);
        listener.update(true, getPlayer1Name());
        listener.update(false, getPlayer2Name());
    }

    /**
     * Deletes the game save file.
     */
    public void deleteSaveFile(){

        File saveFile = new File(SAVE_LOCATION);
        saveFile.delete();

    }

    /**
     * True if a game save file exists, false if it does not.
     * @return
     */
    public boolean gameStateExists(){

        File saveFile = new File(SAVE_LOCATION);
        if(saveFile.exists())
            return true;
        else
            return false;

    }

    /**
     * Returns the gamepiece of the winner.
     * @return
     */
    public GamePiece checkWinner(){
        if (winnerChecker.evaluate(board) > 0)
            return GamePiece.X;

        if (winnerChecker.evaluate(board) < 0)
            return GamePiece.O;

        return null;

    }

    /**
     *
     * @return True if there is a draw, false if there is a winner.
     */
    public boolean checkDraw(){
        if (board.getTurnNumber() == 9 && winnerChecker.evaluate(board) == 0)
            return true;

        return false;

    }

    /**
     * Removes a board listener.
     * @param listener
     */
    public void removeBoardListener(BoardUpdatedListener listener) {
        boardListeners.remove(listener);
    }

    /**
     * Updates the board for each listener.
     */
    public void notifyBoard() {
        boardListeners.forEach(listener -> listener.update(board));
    }

    public void notifyNames() {
        nameListeners.forEach(listener -> listener.update(true, getPlayer1Name()));
        nameListeners.forEach(listener -> listener.update(false, getPlayer2Name()));
    }

    /**
     * Sets the multiplayer status.
     * @param isMultiplayer
     */
    public void setMultiplayer(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
    }

    /**
     * Sets the player1Name text.
     * @param player1Name
     */
    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
        this.notifyNames();
    }

    /**
     * Sets the player2Name text.
     * @param player2Name
     */
    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
        this.notifyNames();
    }

    /**
     * Assigns the evaluator for the inputted difficulty.
     * @param difficulty
     */
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

    /**
     * Generates the decision tree.
     * @param root
     */
    public void awakenMrBill(Node root){
        this.root = root;
        Node.generateTree(this.root);
        this.MrBill = new Minimax(new AdvancedEvaluator(), this.root);
    }

    /**
     *
     * @return Multiplayer status.S
     */
    public boolean isMultiplayer(){return isMultiplayer;}

    /**
     *
     * @return Player1Name text.
     */
    public String getPlayer1Name(){return player1Name;}

    /**
     *
     * @return Player2Name text.
     */
    public String getPlayer2Name(){return player2Name;}

    public void setIP(String serverIP){gameIP = serverIP;}

    public String getIP(){return gameIP;}

    /**
     * Generates a win message depending on who won the game, and the random message.
     * @param winner
     * @return
     */
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

    /**
     * Called when the player makes their move, and it is a singleplayer game, and the ai will make their move.
     */
    public void MakeAMove() {
        board = MrBill.bestMove(root);
    }

    public void setLocalMultiplayerUp() {
        localServer = new BetterServer();
        localServer.fireTheServer();
        client = new ServerConnection();
        client.sendRequest(Request.createLocalMultiplayerRequest(player1Name, player2Name));

        responseInput.start();
    }

//    public void setSinglePlayerUp(boolean mrBillGoesFirst) {
//        localServer = new Server();
//        localServer.start();
//
//        if (mrBillGoesFirst){
//            player1 = new ComputerPlayer(this, MrBill);
//            player2 = new HumanPlayer(this, false);
//        }else{
//            player1 = new HumanPlayer(this, true);
//            player2 = new ComputerPlayer(this, MrBill);
//        }
//
//        player1.start();
//        player2.start();
//
//
//    }

    //setting up online, sets the player to the appropriate host/2nd player
//    public void setOnlineUp(boolean host){
//        if (host){
//            player1 = new HumanPlayer(this, true, getIP());
//            player2 = null;
//            player1.start();
//        }
//        else{
//            player1 = null;
//            player2 = new HumanPlayer(this, false, getIP());
//            player2.start();
//        }
//
//    }

    public void setBoard(Board board){
        this.board.set(board);
    }

    public Board getBoard(){
        return board;
    }

    private Thread responseInput = new Thread(()->{

        boolean gameGoingOn = true;



        while(gameGoingOn) {
            Response response = client.receiveResponse();
            switch (response.getType()) {
                case "MoveValid":
                case "MoveInvalid":
                case "NotYourTurn":
                    board = (Board) response.getData();
                    System.out.println(board);
                    notifyBoard();
                    break;
                case "HostSuccess":
                    System.out.println("Join Code: " + response.getData());
                    break;
                case "HostJoined":
                    setPlayer2Name((String) response.getData());
                    break;
                case "JoinSuccess":
                    setPlayer1Name((String) response.getData());
                    break;
                case "YouWin":
                case "YouLost":
                case "GameDrawn":
                    gameGoingOn = false;
                    break;
                default:
                    System.out.println("Unrecognized Response. Type: " + response.getType());
                    break;
            }
        }

        try {
            TicTacToeApplication.getCoordinator().showWinnerScene();
        }catch(Exception ex){}

    });
}
