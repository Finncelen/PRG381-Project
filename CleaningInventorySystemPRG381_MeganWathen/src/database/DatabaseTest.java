package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {

    public static void main(String[] args) {

        File databaseFile =
                new File("database/cleaning_inventory.db");

        System.out.println(
                "Database file path: "
                + databaseFile.getAbsolutePath()
        );

        System.out.println(
                "Database file exists: "
                + databaseFile.exists()
        );

        try (Connection connection =
                DBConnection.getConnection()) {

            if (connection == null || connection.isClosed()) {
                System.out.println(
                        "The database connection was not opened."
                );
                return;
            }

            DatabaseMetaData metadata =
                    connection.getMetaData();

            System.out.println(
                    "Database connection successful."
            );

            System.out.println(
                    "Database product: "
                    + metadata.getDatabaseProductName()
            );

            System.out.println(
                    "Driver: "
                    + metadata.getDriverName()
            );

            displayTables(connection);

        } catch (SQLException exception) {

            System.err.println(
                    "Database connection failed."
            );

            System.err.println(
                    exception.getMessage()
            );

            exception.printStackTrace();
        }
    }

    private static void displayTables(
            Connection connection
    ) throws SQLException {

        String sql = """
                SELECT name
                FROM sqlite_master
                WHERE type = 'table'
                  AND name NOT LIKE 'sqlite_%'
                ORDER BY name
                """;

        System.out.println("Tables:");

        try (
            Statement statement =
                    connection.createStatement();

            ResultSet resultSet =
                    statement.executeQuery(sql)
        ) {

            while (resultSet.next()) {
                System.out.println(
                        "- " + resultSet.getString("name")
                );
            }
        }
    }
}