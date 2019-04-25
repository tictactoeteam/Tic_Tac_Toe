package edu.saddleback.tictactoe.db;

import edu.saddleback.tictactoe.db.migrations.Init;
import edu.saddleback.tictactoe.db.migrations.Migration;
import java.sql.*;
import java.util.Properties;

/**
 * Represents the database connection on the server, runs the migrations to deal with postgres.
 */
public class DbConnection {

    private static String url = System.getenv("TTT_DB_URL");
    private static String username = System.getenv("TTT_DB_USER");
    private static String password = System.getenv("TTT_DB_PASS");

    /**
     * Returns a connection.
     * @return
     */
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

    /**
     * Runs Migration
     */
    public static void runMigrations() {
        // ADD MIGRATIONS HERE
        Migration[] migrations = { new Init() };
        Connection connection = getConnection();
        int next = lastMigration() + 1;
        if (next < migrations.length) {
            System.out.printf("Running migrations %d - %d\n", next, migrations.length -1);
            for (int i = next; i < migrations.length; i++) {
                try {
                    migrations[i].up(connection);
                    if (i == 0) {
                        connection.prepareStatement("INSERT INTO migrations (last_migration) VALUES (0)").execute();
                    } else {
                        connection.prepareStatement("UPDATE migrations SET last_migration = last_migration + 1").execute();
                    }
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

    /**
     * Finishes the migration.
     * @return
     */
    private static int lastMigration() {
        Connection connection = getConnection();

        try {
            PreparedStatement createTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS migrations" +
                    "(last_migration int)");
            createTable.execute();

            PreparedStatement query = connection.prepareStatement("SELECT last_migration from migrations");
            ResultSet rs = query.executeQuery();

            return rs.next() ? rs.getInt("last_migration") : -1;
        } catch (SQLException e) {
            return -1;
        }
    }
}
