package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initializeDatabase() {

        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT NOT NULL,
                    username TEXT NOT NULL UNIQUE,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL DEFAULT 'Staff',
                    active INTEGER NOT NULL DEFAULT 1
                )
                """;

        String createSuppliersTable = """
                CREATE TABLE IF NOT EXISTS suppliers (
                    supplier_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    supplier_name TEXT NOT NULL UNIQUE,
                    contact_person TEXT,
                    phone_number TEXT,
                    email TEXT,
                    address TEXT
                )
                """;

        String createCleanersTable = """
                CREATE TABLE IF NOT EXISTS cleaners (
                    cleaner_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    employee_number TEXT NOT NULL UNIQUE,
                    phone_number TEXT,
                    email TEXT,
                    department TEXT,
                    active INTEGER NOT NULL DEFAULT 1
                )
                """;

        String createMaterialsTable = """
                CREATE TABLE IF NOT EXISTS materials (
                    material_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    material_name TEXT NOT NULL UNIQUE,
                    description TEXT,
                    quantity_in_stock INTEGER NOT NULL DEFAULT 0,
                    reorder_level INTEGER NOT NULL DEFAULT 0,
                    unit_of_measure TEXT NOT NULL,
                    unit_price REAL NOT NULL DEFAULT 0,
                    supplier_id INTEGER,
                    
                    CHECK (quantity_in_stock >= 0),
                    CHECK (reorder_level >= 0),
                    CHECK (unit_price >= 0),
                    
                    FOREIGN KEY (supplier_id)
                        REFERENCES suppliers(supplier_id)
                        ON DELETE SET NULL
                )
                """;

        String createIssuancesTable = """
                CREATE TABLE IF NOT EXISTS stock_issuances (
                    issuance_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    material_id INTEGER NOT NULL,
                    cleaner_id INTEGER NOT NULL,
                    issued_by_user_id INTEGER,
                    quantity_issued INTEGER NOT NULL,
                    issuance_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    notes TEXT,
                    
                    CHECK (quantity_issued > 0),
                    
                    FOREIGN KEY (material_id)
                        REFERENCES materials(material_id),
                        
                    FOREIGN KEY (cleaner_id)
                        REFERENCES cleaners(cleaner_id),
                        
                    FOREIGN KEY (issued_by_user_id)
                        REFERENCES users(user_id)
                )
                """;

        try (
            Connection connection =
                    DBConnection.getConnection();

            Statement statement =
                    connection.createStatement()
        ) {

            statement.execute(createUsersTable);
            statement.execute(createSuppliersTable);
            statement.execute(createCleanersTable);
            statement.execute(createMaterialsTable);
            statement.execute(createIssuancesTable);

            createIndexes(statement);

            System.out.println(
                    "Database initialized successfully."
            );

        } catch (SQLException exception) {

            System.err.println(
                    "Database initialization failed: "
                    + exception.getMessage()
            );
        }
    }

    private static void createIndexes(
            Statement statement
    ) throws SQLException {

        statement.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_material_supplier
                ON materials(supplier_id)
                """);

        statement.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_issuance_material
                ON stock_issuances(material_id)
                """);

        statement.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_issuance_cleaner
                ON stock_issuances(cleaner_id)
                """);

        statement.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_issuance_date
                ON stock_issuances(issuance_date)
                """);
    }
}
