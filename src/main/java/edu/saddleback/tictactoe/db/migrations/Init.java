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
    }

    @Override
    public void down(Connection conn) throws SQLException {
        conn.prepareStatement("DELETE EXTENSION pgcrypto").execute();
        conn.prepareStatement("DROP TABLE players").execute();
    }
}
