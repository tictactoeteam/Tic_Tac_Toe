package edu.saddleback.tictactoe.db;

import edu.saddleback.tictactoe.model.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The logic behind the player database that handles all upkeeping.
 */
public class PlayerDao {

    public static final String PLAYER_TABLE = "players";
    private static Connection connection = DbConnection.getConnection();

    /**
     * Returns the player from a given ID
     * @param id
     * @return
     * @throws SQLException
     */
    public static Player getPlayerById(String id) throws SQLException {
        String statement = "SELECT * FROM " + PLAYER_TABLE + " WHERE id=?";
        PreparedStatement prepared = connection.prepareStatement(statement);
        prepared.setString(1, id);

        ResultSet rs = prepared.executeQuery();

        return rs.next() ? extractPlayer(rs) : null;
    }

    /**
     * Returns a player by its username.
     * @param username
     * @return
     * @throws SQLException
     */
    public static Player getPlayerByUsername(String username) throws SQLException {
        String statement = "SELECT * FROM " + PLAYER_TABLE + " WHERE username=?";
        PreparedStatement prepared = connection.prepareStatement(statement);
        prepared.setString(1, username);

        ResultSet rs = prepared.executeQuery();

        return rs.next() ? extractPlayer(rs) : null;
    }

    /**
     * Returns all player data.
     * @return
     * @throws SQLException
     */
    public static Player[] getAllPlayers() throws SQLException {
        String statement = "SELECT * FROM " + PLAYER_TABLE;
        PreparedStatement prepared = connection.prepareStatement(statement);

        ResultSet rs = prepared.executeQuery();

        ArrayList<Player> players = new ArrayList<Player>();
        while (rs.next()) {
            players.add(extractPlayer(rs));
        }

        return players.toArray(new Player[players.size()]);
    }

    /**
     * Inserts a new player into the database.
     * @param player
     * @throws SQLException
     */
    public static void insertPlayer(Player player) throws SQLException {
        String statement = "INSERT INTO " + PLAYER_TABLE + " (username, password) VALUES (?, ?)";
        PreparedStatement prepared = connection.prepareStatement(statement);
        prepared.setString(1, player.getUsername());
        prepared.setString(2, player.getHashedPassword());

        int rowsAffected = prepared.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("insertPlayer violated constraint");
        }
    }

    /**
     * Updates a player in the database.
     * @param player
     * @throws SQLException
     */
    public static void updatePlayer(Player player) throws SQLException {
        String statement = "UPDATE " + PLAYER_TABLE + " SET username=?, password=?, disabled=? WHERE id=?";
        PreparedStatement prepared = connection.prepareStatement(statement);
        prepared.setString(1, player.getUsername());
        prepared.setString(2, player.getHashedPassword());
        prepared.setString(3, String.valueOf(player.isDisabled()));
        prepared.setString(4, player.getId().toString());

        int rowsAffected = prepared.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("updatePlayer could not find player with ID " + player.getId());
        }
    }

    /**
     * Returns a player from the database.
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Player extractPlayer(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setId(UUID.fromString(rs.getString("id")));
        player.setUsername(rs.getString("username"));
        player.setHashedPassword(rs.getString("password"));
        player.setDisabled(Boolean.parseBoolean(rs.getString("disabled")));

        return player;
    }
}
