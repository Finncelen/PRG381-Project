package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBConnection {

    private static final String DATABASE_FOLDER = "database";
    private static final String DATABASE_FILE = "cleaning_inventory.db";

    private static final String DATABASE_URL =
            "jdbc:sqlite:"
            + DATABASE_FOLDER
            + File.separator
            + DATABASE_FILE;

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {

        createDatabaseFolder();

        Connection connection =
                DriverManager.getConnection(DATABASE_URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return connection;
    }

    private static void createDatabaseFolder() {

        File folder = new File(DATABASE_FOLDER);

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static boolean testConnection() {

        try (Connection connection = getConnection()) {
            return connection != null
                    && !connection.isClosed();

        } catch (SQLException exception) {
            System.err.println(
                    "Database connection failed: "
                    + exception.getMessage()
            );

            return false;
        }
    }
}