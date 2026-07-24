package dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import models.StockIssuance;
import utils.IDGenerator;

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
}