package edu.saddleback.tictactoe.db.migrations;

import java.sql.Connection;
import java.sql.SQLException;

public class Init implements Migration {

    @Override
    public void up(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE EXTENSION IF NOT EXISTS pgcrypto").execute();

        conn.prepareStatement(
                "CREATE TABLE players " +
                "(id text PRIMARY KEY DEFAULT gen_random_uuid()," +
                "username text NOT NULL," +
                "password text NOT NULL)"
        ).execute();

        conn.prepareStatement("CREATE INDEX players_username ON players (username)").execute();

        conn.prepareStatement(
                "CREATE TABLE games " +
                   "(id text PRIMARY KEY DEFAULT gen_random_uuid()," +
                   "player_x text NOT NULL REFERENCES players(id)," +
                   "player_o text NOT NULL REFERENCES players(id)," +
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
