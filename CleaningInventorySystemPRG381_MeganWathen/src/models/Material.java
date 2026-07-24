
package models;

/**
 *
 * @author Finncelen
 */

public class Material {

    private int materialId;
    private String materialName;
    private String description;
    private int quantityInStock;
    private int reorderLevel;
    private String unitOfMeasure;
    private double unitPrice;
    private int supplierId;

    public Material() {
    }

    public Material(
            int materialId,
            String materialName,
            String description,
            int quantityInStock,
            int reorderLevel,
            String unitOfMeasure,
            double unitPrice,
            int supplierId
    ) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.description = description;
        this.quantityInStock = quantityInStock;
        this.reorderLevel = reorderLevel;
        this.unitOfMeasure = unitOfMeasure;
        this.unitPrice = unitPrice;
        this.supplierId = supplierId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        if (quantityInStock < 0) {
            throw new IllegalArgumentException(
                    "Stock quantity cannot be negative."
            );
        }

        this.quantityInStock = quantityInStock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        if (reorderLevel < 0) {
            throw new IllegalArgumentException(
                    "Reorder level cannot be negative."
            );
        }

        this.reorderLevel = reorderLevel;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException(
                    "Unit price cannot be negative."
            );
        }

        this.unitPrice = unitPrice;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public boolean isLowStock() {
        return quantityInStock <= reorderLevel;
    }

    public void deductStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Issued quantity must be greater than zero."
            );
        }

        if (quantity > quantityInStock) {
            throw new IllegalArgumentException(
                    "There is not enough stock available."
            );
        }

        quantityInStock -= quantity;
    }

    @Override
    public String toString() {
        return materialName;
    }
}
