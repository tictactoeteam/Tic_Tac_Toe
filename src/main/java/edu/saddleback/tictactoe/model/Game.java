package edu.saddleback.tictactoe.model;

import java.util.Arrays;
import java.util.Objects;

public class Game {
    private String id;
    private Player playerX;
    private Player playerO;

    private byte[] moves;

    public Game(Player playerX, Player playerO, byte[] moves) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.moves = moves;
    }

    public Game(Player playerX, Player playerO) {
        this(playerX, playerO, new byte[9]);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player getPlayerX() {
        return playerX;
    }

    public void setPlayerX(Player playerX) {
        this.playerX = playerX;
    }

    public Player getPlayerO() {
        return playerO;
    }

    public void setPlayerO(Player playerO) {
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
