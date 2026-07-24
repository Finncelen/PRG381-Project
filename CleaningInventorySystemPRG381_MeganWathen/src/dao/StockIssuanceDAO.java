package dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import models.StockIssuance;
import utils.IDGenerator;
import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockIssuanceDAO {

    private static final List<StockIssuance> issuances =
            new ArrayList<>();

    private final MaterialDAO materialDAO;

    public StockIssuanceDAO() {
        materialDAO = new MaterialDAO();
    }

    public boolean addIssuance(StockIssuance issuance) {
        if (issuance == null) {
            return false;
        }

        if (issuance.getQuantityIssued() <= 0) {
            return false;
        }

        boolean stockIssued = materialDAO.issueStock(
                issuance.getMaterialId(),
                issuance.getQuantityIssued()
        );

        if (!stockIssued) {
            return false;
        }

        issuance.setIssuanceId(IDGenerator.nextIssuanceID());

        if (issuance.getIssuanceDate() == null) {
            issuance.setIssuanceDate(LocalDateTime.now());
        }

        issuances.add(issuance);
        return true;
    }

    public boolean updateIssuance(StockIssuance updatedIssuance) {
        if (updatedIssuance == null) {
            return false;
        }

        for (int i = 0; i < issuances.size(); i++) {
            StockIssuance currentIssuance = issuances.get(i);

            if (currentIssuance.getIssuanceId()
                    == updatedIssuance.getIssuanceId()) {

                issuances.set(i, updatedIssuance);
                return true;
            }
        }

        return false;
    }

    public boolean deleteIssuance(int issuanceId) {
        return issuances.removeIf(
                issuance -> issuance.getIssuanceId() == issuanceId
        );
    }

    public StockIssuance getIssuanceById(int issuanceId) {
        for (StockIssuance issuance : issuances) {
            if (issuance.getIssuanceId() == issuanceId) {
                return issuance;
            }
        }

        return null;
    }

    public List<StockIssuance> getAllIssuances() {
        return new ArrayList<>(issuances);
    }

    public List<StockIssuance> getIssuancesByCleaner(int cleanerId) {
        List<StockIssuance> results = new ArrayList<>();

        for (StockIssuance issuance : issuances) {
            if (issuance.getCleanerId() == cleanerId) {
                results.add(issuance);
            }
        }

        return results;
    }

    public List<StockIssuance> getIssuancesByMaterial(int materialId) {
        List<StockIssuance> results = new ArrayList<>();

        for (StockIssuance issuance : issuances) {
            if (issuance.getMaterialId() == materialId) {
                results.add(issuance);
            }
        }

        return results;
    }

    public List<StockIssuance> getIssuancesByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        List<StockIssuance> results = new ArrayList<>();

        if (startDate == null || endDate == null) {
            return results;
        }

        for (StockIssuance issuance : issuances) {
            LocalDateTime issuanceDate =
                    issuance.getIssuanceDate();

            boolean isWithinRange =
                    !issuanceDate.isBefore(startDate)
                    && !issuanceDate.isAfter(endDate);

            if (isWithinRange) {
                results.add(issuance);
            }
        }

        return results;
    }

    public int getTotalQuantityIssued() {
        int total = 0;

        for (StockIssuance issuance : issuances) {
            total += issuance.getQuantityIssued();
        }

        return total;
    }
    public boolean issueStock(
        int materialId,
        int cleanerId,
        int issuedByUserId,
        int quantity,
        String notes
) {

    if (quantity <= 0) {
        return false;
    }

    String getStockSql = """
            SELECT quantity_in_stock
            FROM materials
            WHERE material_id = ?
            """;

    String insertIssuanceSql = """
            INSERT INTO stock_issuances (
                material_id,
                cleaner_id,
                issued_by_user_id,
                quantity_issued,
                issuance_date,
                notes
            )
            VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?)
            """;

    String reduceStockSql = """
            UPDATE materials
            SET quantity_in_stock =
                quantity_in_stock - ?
            WHERE material_id = ?
              AND quantity_in_stock >= ?
            """;

    Connection connection = null;

    try {

        connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        int availableStock;

        try (
            PreparedStatement stockStatement =
                    connection.prepareStatement(getStockSql)
        ) {

            stockStatement.setInt(1, materialId);

            try (
                ResultSet resultSet =
                        stockStatement.executeQuery()
            ) {

                if (!resultSet.next()) {
                    connection.rollback();
                    return false;
                }

                availableStock =
                        resultSet.getInt(
                                "quantity_in_stock"
                        );
            }
        }

        if (availableStock < quantity) {
            connection.rollback();

            throw new SQLException(
                    "Insufficient stock. Available: "
                    + availableStock
            );
        }

        try (
            PreparedStatement issuanceStatement =
                    connection.prepareStatement(
                            insertIssuanceSql
                    )
        ) {

            issuanceStatement.setInt(1, materialId);
            issuanceStatement.setInt(2, cleanerId);

            if (issuedByUserId > 0) {
                issuanceStatement.setInt(
                        3,
                        issuedByUserId
                );
            } else {
                issuanceStatement.setNull(
                        3,
                        java.sql.Types.INTEGER
                );
            }

            issuanceStatement.setInt(4, quantity);
            issuanceStatement.setString(5, notes);

            issuanceStatement.executeUpdate();
        }

        try (
            PreparedStatement stockUpdateStatement =
                    connection.prepareStatement(
                            reduceStockSql
                    )
        ) {

            stockUpdateStatement.setInt(1, quantity);
            stockUpdateStatement.setInt(2, materialId);
            stockUpdateStatement.setInt(3, quantity);

            int updatedRows =
                    stockUpdateStatement.executeUpdate();

            if (updatedRows == 0) {
                connection.rollback();
                return false;
            }
        }

        connection.commit();
        return true;

    } catch (SQLException exception) {

        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                System.err.println(
                        rollbackException.getMessage()
                );
            }
        }

        System.err.println(
                "Stock issuance failed: "
                + exception.getMessage()
        );

        return false;

    } finally {

        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();

            } catch (SQLException exception) {
                System.err.println(
                        exception.getMessage()
                );
            }
        }
    }
}
}