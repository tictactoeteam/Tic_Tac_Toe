package edu.saddleback.tictactoe.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Game {
    private UUID id;
    private String  playerX;
    private String playerO;

    private byte[] moves;


    private Board board;

    public Game(UUID id, String playerX, String playerO, byte[] moves) {
        this.id = id;
        this.playerX = playerX;
        this.playerO = playerO;
        this.board = new Board();
        this.moves = moves;
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

    public String getPlayerX() {
        return playerX;
    }

    public void setPlayerX(String playerX) {
        this.playerX = playerX;
    }

    public String getPlayerO() {
        return playerO;
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
