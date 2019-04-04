package edu.saddleback.tictactoe.db;

import edu.saddleback.tictactoe.db.migrations.Init;
import edu.saddleback.tictactoe.db.migrations.Migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
    private static String url = System.getenv("TTT_DB_URL");
    private static String username = System.getenv("TTT_DB_USER");
    private static String password = System.getenv("TTT_DB_PASS");

    public static Connection getConnection() {
        if (url == null || username == null || password == null) {
            System.err.println("Missing environment variables, set:");
            System.err.println(" - TTT_DB_URL");
            System.err.println(" - TTT_DB_USER");
            System.err.println(" - TTT_DB_PASS");
            System.exit(1);
        }

        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Missing Postgres driver - unable to start server");
            System.exit(1);
        }

        return null;
    }

    public static void runMigrations(int from) {
        // ADD MIGRATIONS HERE
        Migration[] migrations = { new Init() };
        Connection connection = getConnection();
        for (int i = from; i < migrations.length; i++) {
            try {
                migrations[i].up(connection);
            } catch (SQLException e) {
                try {
                     migrations[i].down(connection);
                    System.err.println("Migration failed, but was successfully reverted.");
                    System.err.println("Exiting");
                    System.exit(1);
                } catch (SQLException ex) {
                    System.err.println("Migration failed, AND failed to rollback.");
                    System.err.println("You're probably completely fucked");
                    System.exit(1);
                }
            }
        }
    }
}
