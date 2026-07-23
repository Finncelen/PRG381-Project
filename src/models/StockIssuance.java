
package models;
import java.time.LocalDateTime;
/**
 *
 * @author Finncelen
 */



public class StockIssuance {

    private int issuanceId;
    private int materialId;
    private int cleanerId;
    private int issuedByUserId;
    private int quantityIssued;
    private LocalDateTime issuanceDate;
    private String notes;

    public StockIssuance() {
        this.issuanceDate = LocalDateTime.now();
    }

    public StockIssuance(
            int issuanceId,
            int materialId,
            int cleanerId,
            int issuedByUserId,
            int quantityIssued,
            LocalDateTime issuanceDate,
            String notes
    ) {
        this.issuanceId = issuanceId;
        this.materialId = materialId;
        this.cleanerId = cleanerId;
        this.issuedByUserId = issuedByUserId;
        this.quantityIssued = quantityIssued;
        this.issuanceDate = issuanceDate;
        this.notes = notes;
    }

    public int getIssuanceId() {
        return issuanceId;
    }

    public void setIssuanceId(int issuanceId) {
        this.issuanceId = issuanceId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getCleanerId() {
        return cleanerId;
    }

    public void setCleanerId(int cleanerId) {
        this.cleanerId = cleanerId;
    }

    public int getIssuedByUserId() {
        return issuedByUserId;
    }

    public void setIssuedByUserId(int issuedByUserId) {
        this.issuedByUserId = issuedByUserId;
    }

    public int getQuantityIssued() {
        return quantityIssued;
    }

    public void setQuantityIssued(int quantityIssued) {
        if (quantityIssued <= 0) {
            throw new IllegalArgumentException(
                    "Issued quantity must be greater than zero."
            );
        }

        this.quantityIssued = quantityIssued;
    }

    public LocalDateTime getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(LocalDateTime issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
