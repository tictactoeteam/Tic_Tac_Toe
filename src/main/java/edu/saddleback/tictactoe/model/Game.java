package edu.saddleback.tictactoe.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Game {
    private UUID id;
    private String  playerX;
    private Player playerObjectX;
    private String playerO;
    private Player playerObjectO;

    private byte[] moves;


    private Board board;

    public Game(UUID id, Player playerX, Player playerO, byte[] moves) {
        this.id = id;
        this.playerObjectX = playerX;
        this.playerObjectO = playerO;
        this.moves = moves;

        this.board = new Board();

        this.playerX = playerObjectX.getUsername();
        this.playerO = playerObjectO.getUsername();
    }

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

    public Game(String playerX, String playerO) {
        this(UUID.randomUUID(), playerX, playerO, new byte[9]);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Player getPlayerX() {
        return playerObjectX;
    }

    public void setPlayerX(String playerX) {
        this.playerX = playerX;
    }

    public Player getPlayerO() {
        return playerObjectO;
    }

    public Board getBoard(){
        return board;
    }

    public void setPlayerO(String playerO) {
        this.playerO = playerO;
    }

    public byte[] getMoves() {
        return moves;
    }

    public void setMoves(byte[] moves) {
        this.moves = moves;
    }

    public byte getMove(int turn) {
        return moves[turn];
    }

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
