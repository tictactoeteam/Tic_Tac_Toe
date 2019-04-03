package edu.saddleback.tictactoe.db;

import edu.saddleback.tictactoe.model.Game;
import edu.saddleback.tictactoe.model.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameDao {
    public static final String GAMES_TABLE = "games";
    private static Connection connection = DbConnection.getConnection();

    public static Game getGameById(String id) throws SQLException {
        String statement = "SELECT g.id, g.moves, " +
                "player_x, px.username AS player_x_username, player_o, po.username AS player_o_username" +
                "FROM " + GAMES_TABLE + " g " +
                "INNER JOIN " + PlayerDao.PLAYER_TABLE + " px ON px.id = g.player_x " +
                "INNER JOIN " + PlayerDao.PLAYER_TABLE + " po ON po.id = g.player_o" +
                "WHERE g.id=?";

        PreparedStatement prepared = connection.prepareStatement(statement);
        prepared.setString(1, id);

        ResultSet rs = prepared.executeQuery();

        return rs.next() ? extractGame(rs) : null;
    }

    public static Game[] getAllGames() throws SQLException {
        String statement = "SELECT g.id, g.moves, " +
                "player_x, px.username AS player_x_username, player_o, po.username AS player_o_username" +
                "FROM " + GAMES_TABLE + " g " +
                "INNER JOIN " + PlayerDao.PLAYER_TABLE + " px ON px.id = g.player_x " +
                "INNER JOIN " + PlayerDao.PLAYER_TABLE + " po ON po.id = g.player_o";

        PreparedStatement prepared = connection.prepareStatement(statement);

        ResultSet rs = prepared.executeQuery();

        ArrayList<Game> games = new ArrayList<>();
        while (rs.next()) {
            games.add(extractGame(rs));
        }

        return games.toArray(new Game[games.size()]);
    }

    public static void insertGame(Game game) throws SQLException {
        String statement = "INSERT INTO " + GAMES_TABLE + " (player_x, player_o, moves) VALUES (?, ?, ?)";
        PreparedStatement prepared = connection.prepareStatement(statement);
        prepared.setString(1, game.getPlayerX().getId());
        prepared.setString(2, game.getPlayerO().getId());

        Byte[] moves = new Byte[game.getMoves().length];
        for (int i = 0; i < game.getMoves().length; i++) {
            moves[i] = game.getMove(i);
        }

        prepared.setArray(3, connection.createArrayOf("smallint", moves));

        int rowsAffected = prepared.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("insertGame violated constraint");
        }
    }

    public static void updateGame(Game game) throws SQLException {
        String statement = "UPDATE " + GAMES_TABLE + " SET player_x=?, player_o=?, moves=? WHERE id=?";
        PreparedStatement prepared = connection.prepareStatement(statement);
        prepared.setString(1, game.getPlayerX().getId());
        prepared.setString(2, game.getPlayerO().getId());

        Byte[] moves = new Byte[game.getMoves().length];
        for (int i = 0; i < game.getMoves().length; i++) {
            moves[i] = game.getMove(i);
        }

        prepared.setArray(3, connection.createArrayOf("smallint", moves));
        prepared.setString(4, game.getId());

        int rowsAffected = prepared.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("Failed to update game, possibly the game or the players do not exist");
        }
    }

    public static void deleteGame(String id) throws SQLException {
        String statement = "DELETE FROM " + GAMES_TABLE + "WHERE id=?";
        PreparedStatement prepared = connection.prepareStatement(statement);
        prepared.setString(1, id);

        int rowsAffected = prepared.executeUpdate(statement);

        if (rowsAffected == 0) {
            throw new SQLException("deleteGame could not find game with ID " + id);
        }
    }

    private static Game extractGame(ResultSet rs) throws SQLException {
        String playerXId = rs.getString("player_x");
        String playerXName = rs.getString("player_x_username");
        Player playerX = new Player();
        playerX.setId(playerXId);
        playerX.setUsername(playerXName);

        String playerOId = rs.getString("player_o");
        String playerOName = rs.getString("player_o_username");
        Player playerO = new Player();
        playerO.setId(playerOId);
        playerO.setUsername(playerOName);

        byte[] moves = (byte[]) rs.getArray("moves").getArray();
        return new Game(playerX, playerO, moves);
    }
}
