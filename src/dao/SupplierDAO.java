package dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Supplier;

public class SupplierDAO {

    public boolean addSupplier(Supplier supplier) {

        String sql = """
                INSERT INTO suppliers (
                    supplier_name,
                    contact_person,
                    phone_number,
                    email,
                    address
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(1, supplier.getSupplierName());
            statement.setString(2, supplier.getContactPerson());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getEmail());
            statement.setString(5, supplier.getAddress());

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            System.err.println(
                    "Error adding supplier: "
                    + exception.getMessage()
            );

            return false;
        }
    }

    public boolean updateSupplier(Supplier supplier) {

        String sql = """
                UPDATE suppliers
                SET supplier_name = ?,
                    contact_person = ?,
                    phone_number = ?,
                    email = ?,
                    address = ?
                WHERE supplier_id = ?
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(1, supplier.getSupplierName());
            statement.setString(2, supplier.getContactPerson());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getEmail());
            statement.setString(5, supplier.getAddress());
            statement.setInt(6, supplier.getSupplierId());

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            System.err.println(
                    "Error updating supplier: "
                    + exception.getMessage()
            );

            return false;
        }
    }

    public boolean deleteSupplier(int supplierId) {

        String sql = """
                DELETE FROM suppliers
                WHERE supplier_id = ?
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setInt(1, supplierId);

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            System.err.println(
                    "Error deleting supplier: "
                    + exception.getMessage()
            );

            return false;
        }
    }

    public Supplier getSupplierById(int supplierId) {

        String sql = """
                SELECT *
                FROM suppliers
                WHERE supplier_id = ?
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setInt(1, supplierId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapSupplier(resultSet);
                }
            }

        } catch (SQLException exception) {
            System.err.println(
                    "Error retrieving supplier: "
                    + exception.getMessage()
            );
        }

        return null;
    }

    public List<Supplier> getAllSuppliers() {

        List<Supplier> suppliers = new ArrayList<>();

        String sql = """
                SELECT *
                FROM suppliers
                ORDER BY supplier_name
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                suppliers.add(mapSupplier(resultSet));
            }

        } catch (SQLException exception) {
            System.err.println(
                    "Error retrieving suppliers: "
                    + exception.getMessage()
            );
        }

        return suppliers;
    }

    public boolean supplierNameExists(String supplierName) {

        String sql = """
                SELECT 1
                FROM suppliers
                WHERE LOWER(supplier_name) = LOWER(?)
                LIMIT 1
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(1, supplierName.trim());

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException exception) {
            System.err.println(
                    "Error checking supplier name: "
                    + exception.getMessage()
            );

            return false;
        }
    }

    private Supplier mapSupplier(ResultSet resultSet)
            throws SQLException {

        Supplier supplier = new Supplier();

        supplier.setSupplierId(
                resultSet.getInt("supplier_id")
        );

        supplier.setSupplierName(
                resultSet.getString("supplier_name")
        );

        supplier.setContactPerson(
                resultSet.getString("contact_person")
        );

        supplier.setPhoneNumber(
                resultSet.getString("phone_number")
        );

        supplier.setEmail(
                resultSet.getString("email")
        );

        supplier.setAddress(
                resultSet.getString("address")
        );

        return supplier;
    }
    
    public List<Supplier> searchSuppliers(String searchText) {

    List<Supplier> suppliers = new ArrayList<>();

    String sql = """
        SELECT *
        FROM suppliers
        WHERE supplier_name LIKE ?
           OR contact_person LIKE ?
           OR email LIKE ?
        """;

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {

        String search = "%" + searchText + "%";

        statement.setString(1, search);
        statement.setString(2, search);
        statement.setString(3, search);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {

            Supplier supplier = new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("contact_person"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    rs.getString("address")
            );

            suppliers.add(supplier);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return suppliers;
}
}