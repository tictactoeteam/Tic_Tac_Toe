package edu.saddleback.tictactoe.db.migrations;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Does something idk ask David
 */
public class Init implements Migration {

    @Override
    public void up(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE EXTENSION IF NOT EXISTS pgcrypto").execute();

        conn.prepareStatement(
                "CREATE TABLE players " +
                "(id uuid PRIMARY KEY DEFAULT gen_random_uuid()," +
                "username text UNIQUE NOT NULL," +
                "password text NOT NULL, " +
                "disabled boolean NOT NULL DEFAULT FALSE)"
        ).execute();

        conn.prepareStatement("CREATE INDEX players_username ON players (username)").execute();

        conn.prepareStatement(
                "CREATE TABLE games " +
                   "(id uuid PRIMARY KEY DEFAULT gen_random_uuid()," +
                   "player_x uuid NOT NULL REFERENCES players(id)," +
                   "player_o uuid NOT NULL REFERENCES players(id)," +
                   "moves smallint[] NOT NULL)"
        ).execute();

        conn.prepareStatement("CREATE INDEX games_x ON games (player_x)").execute();
        conn.prepareStatement("CREATE INDEX games_o ON games (player_o)").execute();
    }

    @Override
    public void down(Connection conn) throws SQLException {
        conn.prepareStatement("DROP INDEX games_x").execute();
        conn.prepareStatement("DROP INDEX games_o").execute();
        conn.prepareStatement("DROP INDEX players_username").execute();
        conn.prepareStatement("DROP TABLE games").execute();
        conn.prepareStatement("DROP TABLE players").execute();
        conn.prepareStatement("DROP EXTENSION pgcrypto").execute();
    }
}
