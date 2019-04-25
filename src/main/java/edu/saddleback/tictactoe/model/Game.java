package edu.saddleback.tictactoe.model;

import edu.saddleback.tictactoe.decision.Minimax;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a current game in the server.
 */
public class Game {
    private UUID id;
    private String  playerX;
    private Player playerObjectX;
    private String playerO;
    private Player playerObjectO;
    private byte[] moves;
    private Board board;

    /**
     * Constructor
     * @param id
     * @param playerX
     * @param playerO
     * @param moves
     */
    public Game(UUID id, Player playerX, Player playerO, byte[] moves) {
        this.id = id;
        this.playerObjectX = playerX;
        this.playerObjectO = playerO;
        this.moves = moves;

        this.board = new Board();

        this.playerX = playerObjectX.getUsername();
        this.playerO = playerObjectO.getUsername();
    }

    /**
     * Constructor
     * @param id
     * @param playerX
     * @param playerO
     * @param moves
     */
    public Game(UUID id, String playerX, String playerO, byte[] moves) {

        this.id = id;
        this.playerX = playerX;
        this.playerO = playerO;
        this.board = new Board();
        this.moves = moves;
        playerObjectX = new Player();
        playerObjectX.setUsername(playerX);
        playerObjectO = new Player();
        playerObjectO.setUsername(playerO);
    }

    /**
     * Constructor
     * @param playerX
     * @param playerO
     */
    public Game(String playerX, String playerO) {
        this(UUID.randomUUID(), playerX, playerO, new byte[9]);
    }

    /**
     * Mr Bill Constructor
     * Instantitaes a game instance of Mr.Bill. Takes in only
     * one player, which by default is assigned to O.
     * @param playerO
     */
    public Game(String playerO, Minimax mrBill){
        this(UUID.randomUUID(), "Mr. Bill", playerO, new byte[9]);
    }

    /**
     * Returns UUID
     * @return
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets UUID
     * @param id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets playerX
     * @return
     */
    public Player getPlayerX() {
        return playerObjectX;
    }

    /**
     * Sets playerX
     * @param playerX
     */
    public void setPlayerX(String playerX) {
        this.playerX = playerX;
    }

    /**
     * Gets playerO
     * @return
     */
    public Player getPlayerO() {
        return playerObjectO;
    }

    /**
     * Gets the board
     * @return
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Sets playerO
     * @param playerO
     */
    public void setPlayerO(String playerO) {
        this.playerO = playerO;
    }

    /**
     * Gets all the moves' indecies
     * @return
     */
    public byte[] getMoves() {
        for (int i = 0; i < board.getHistory().length; i++) {
            this.moves[i] = (byte) board.getHistory()[i];
        }

        return this.moves;
    }

    /**
     * Sets the move indexies
     * @param moves
     */
    public void setMoves(byte[] moves) {
        this.moves = moves;
    }

    /**
     * Gets a move
     * @param turn
     * @return
     */
    public byte getMove(int turn) {
        return moves[turn];
    }

    /**
     * Sets a move
     * @param turn
     * @param move
     */
    public void setMove(int turn, byte move) {
        this.moves[turn] = move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(playerX, game.playerX) &&
                Objects.equals(playerO, game.playerO) &&
                Arrays.equals(moves, game.moves);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(playerX, playerO);
        result = 31 * result + Arrays.hashCode(moves);
        return result;
    }
}
