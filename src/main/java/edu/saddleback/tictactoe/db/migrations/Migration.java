package edu.saddleback.tictactoe.db.migrations;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Migration Interface
 */
public interface Migration {
    void up(Connection conn) throws SQLException;
    void down(Connection conn) throws SQLException;
}
