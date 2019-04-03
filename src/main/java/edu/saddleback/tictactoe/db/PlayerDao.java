package edu.saddleback.tictactoe.db;

import edu.saddleback.tictactoe.model.Player;

import java.sql.*;
import java.util.ArrayList;

public class PlayerDao {
    private static final String PLAYER_TABLE = "players";
    private static Connection connection = DbConnection.getConnection();

    public static Player getPlayerById(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM ? WHERE id=?"
        );
        statement.setString(1, PLAYER_TABLE);
        statement.setString(2, id);

        ResultSet rs = statement.executeQuery();

        return rs.next() ? extractPlayer(rs) : null;
    }

    public static Player getPlayerByUsername(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM ? WHERE username=?"
        );
        statement.setString(1, PLAYER_TABLE);
        statement.setString(2, username);

        ResultSet rs = statement.executeQuery();

        return rs.next() ? extractPlayer(rs) : null;
    }

    public static Player[] getAllPlayers() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM ?"
        );
        statement.setString(1, PLAYER_TABLE);

        ResultSet rs = statement.executeQuery();

        ArrayList<Player> players = new ArrayList<Player>();
        while (rs.next()) {
            players.add(extractPlayer(rs));
        }

        return players.toArray(new Player[players.size()]);
    }

    public static void insertPlayer(Player player) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO ? (username, password) VALUES (?, ?)"
        );
        statement.setString(1, PLAYER_TABLE);
        statement.setString(2, player.getUsername());
        statement.setString(3, player.getHashedPassword());

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Failed to insert, constraint violated");
        }

        String id = statement.getGeneratedKeys().getString("id");
        player.setId(id);
    }

    public static void updatePlayer(Player player) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE ? SET username=?, password=? WHERE id=?"
        );
        statement.setString(1, PLAYER_TABLE);
        statement.setString(2, player.getUsername());
        statement.setString(3, player.getHashedPassword());
        statement.setString(4, player.getId());

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Could not find player");
        }
    }

    public static void deletePlayer(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM ? WHERE id=?"
        );
        statement.setString(1, PLAYER_TABLE);
        statement.setString(2, id);

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Could not find player");
        }
    }

    private static Player extractPlayer(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setId(rs.getString("id"));
        player.setUsername(rs.getString("username"));
        player.setHashedPassword(rs.getString("password"));

        return player;
    }
}
