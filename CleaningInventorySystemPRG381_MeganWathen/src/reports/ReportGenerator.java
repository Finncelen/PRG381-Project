package reports;

import dao.CleanerDAO;
import dao.MaterialDAO;
import dao.StockIssuanceDAO;
import dao.SupplierDAO;
import java.time.LocalDateTime;
import java.util.List;
import models.Cleaner;
import models.Material;
import models.StockIssuance;
import models.Supplier;
import utils.DateHelper;

public class ReportGenerator {

    private final MaterialDAO materialDAO;
    private final SupplierDAO supplierDAO;
    private final CleanerDAO cleanerDAO;
    private final StockIssuanceDAO stockIssuanceDAO;

    public ReportGenerator() {
        materialDAO = new MaterialDAO();
        supplierDAO = new SupplierDAO();
        cleanerDAO = new CleanerDAO();
        stockIssuanceDAO = new StockIssuanceDAO();
    }

    public String generateSummaryReport() {
        StringBuilder report = new StringBuilder();

        List<Material> materials = materialDAO.getAllMaterials();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        List<Cleaner> cleaners = cleanerDAO.getAllCleaners();
        List<StockIssuance> issuances =
                stockIssuanceDAO.getAllIssuances();

        int activeCleaners = 0;
        int totalStock = 0;
        double totalInventoryValue = 0;

        for (Cleaner cleaner : cleaners) {
            if (cleaner.isActive()) {
                activeCleaners++;
            }
        }

        for (Material material : materials) {
            totalStock += material.getQuantityInStock();

            totalInventoryValue +=
                    material.getQuantityInStock()
                    * material.getUnitPrice();
        }

        report.append("UNIVERSITY CLEANING INVENTORY SYSTEM\n");
        report.append("SUMMARY REPORT\n");
        report.append("====================================\n\n");

        report.append("Generated: ")
                .append(DateHelper.getCurrentDateTime())
                .append("\n\n");

        report.append("Total suppliers: ")
                .append(suppliers.size())
                .append("\n");

        report.append("Total materials: ")
                .append(materials.size())
                .append("\n");

        report.append("Total stock units: ")
                .append(totalStock)
                .append("\n");

        report.append("Estimated inventory value: R")
                .append(String.format("%.2f", totalInventoryValue))
                .append("\n");

        report.append("Total cleaners: ")
                .append(cleaners.size())
                .append("\n");

        report.append("Active cleaners: ")
                .append(activeCleaners)
                .append("\n");

        report.append("Inactive cleaners: ")
                .append(cleaners.size() - activeCleaners)
                .append("\n");

        report.append("Low-stock materials: ")
                .append(materialDAO.getLowStockMaterials().size())
                .append("\n");

        report.append("Stock issuance records: ")
                .append(issuances.size())
                .append("\n");

        report.append("Total quantity issued: ")
                .append(stockIssuanceDAO.getTotalQuantityIssued())
                .append("\n");

        return report.toString();
    }

    public String generateMaterialsReport() {
        StringBuilder report = new StringBuilder();

        List<Material> materials =
                materialDAO.getAllMaterials();

        report.append("MATERIAL INVENTORY REPORT\n");
        report.append("=========================\n\n");

        report.append("Generated: ")
                .append(DateHelper.getCurrentDateTime())
                .append("\n\n");

        if (materials.isEmpty()) {
            report.append("No materials are currently registered.\n");
            return report.toString();
        }

        for (Material material : materials) {
            Supplier supplier = supplierDAO.getSupplierById(
                    material.getSupplierId()
            );

            report.append("Material ID: ")
                    .append(material.getMaterialId())
                    .append("\n");

            report.append("Material name: ")
                    .append(material.getMaterialName())
                    .append("\n");

            report.append("Description: ")
                    .append(material.getDescription())
                    .append("\n");

            report.append("Quantity in stock: ")
                    .append(material.getQuantityInStock())
                    .append(" ")
                    .append(material.getUnitOfMeasure())
                    .append("\n");

            report.append("Reorder level: ")
                    .append(material.getReorderLevel())
                    .append("\n");

            report.append("Unit price: R")
                    .append(String.format(
                            "%.2f",
                            material.getUnitPrice()
                    ))
                    .append("\n");

            report.append("Stock value: R")
                    .append(String.format(
                            "%.2f",
                            material.getQuantityInStock()
                            * material.getUnitPrice()
                    ))
                    .append("\n");

            report.append("Supplier: ")
                    .append(
                            supplier == null
                                    ? "Not assigned"
                                    : supplier.getSupplierName()
                    )
                    .append("\n");

            report.append("Stock status: ")
                    .append(
                            material.isLowStock()
                                    ? "LOW STOCK"
                                    : "Sufficient"
                    )
                    .append("\n");

            report.append("----------------------------------------\n");
        }

        return report.toString();
    }

    public String generateLowStockReport() {
        StringBuilder report = new StringBuilder();

        List<Material> lowStockMaterials =
                materialDAO.getLowStockMaterials();

        report.append("LOW-STOCK REPORT\n");
        report.append("================\n\n");

        report.append("Generated: ")
                .append(DateHelper.getCurrentDateTime())
                .append("\n\n");

        if (lowStockMaterials.isEmpty()) {
            report.append(
                    "No materials are currently below or equal "
                    + "to their reorder levels.\n"
            );

            return report.toString();
        }

        for (Material material : lowStockMaterials) {
            int suggestedOrderQuantity =
                    (material.getReorderLevel() * 2)
                    - material.getQuantityInStock();

            if (suggestedOrderQuantity < 1) {
                suggestedOrderQuantity = 1;
            }

            report.append("Material: ")
                    .append(material.getMaterialName())
                    .append("\n");

            report.append("Current stock: ")
                    .append(material.getQuantityInStock())
                    .append(" ")
                    .append(material.getUnitOfMeasure())
                    .append("\n");

            report.append("Reorder level: ")
                    .append(material.getReorderLevel())
                    .append("\n");

            report.append("Suggested order quantity: ")
                    .append(suggestedOrderQuantity)
                    .append(" ")
                    .append(material.getUnitOfMeasure())
                    .append("\n");

            report.append("----------------------------------------\n");
        }

        return report.toString();
    }

    public String generateSupplierReport() {
        StringBuilder report = new StringBuilder();

        List<Supplier> suppliers =
                supplierDAO.getAllSuppliers();

        report.append("SUPPLIER REPORT\n");
        report.append("===============\n\n");

        report.append("Generated: ")
                .append(DateHelper.getCurrentDateTime())
                .append("\n\n");

        if (suppliers.isEmpty()) {
            report.append("No suppliers are currently registered.\n");
            return report.toString();
        }

        for (Supplier supplier : suppliers) {
            int suppliedMaterialCount = 0;

            for (Material material : materialDAO.getAllMaterials()) {
                if (material.getSupplierId()
                        == supplier.getSupplierId()) {

                    suppliedMaterialCount++;
                }
            }

            report.append("Supplier ID: ")
                    .append(supplier.getSupplierId())
                    .append("\n");

            report.append("Supplier name: ")
                    .append(supplier.getSupplierName())
                    .append("\n");

            report.append("Contact person: ")
                    .append(supplier.getContactPerson())
                    .append("\n");

            report.append("Phone number: ")
                    .append(supplier.getPhoneNumber())
                    .append("\n");

            report.append("Email address: ")
                    .append(supplier.getEmail())
                    .append("\n");

            report.append("Address: ")
                    .append(supplier.getAddress())
                    .append("\n");

            report.append("Materials supplied: ")
                    .append(suppliedMaterialCount)
                    .append("\n");

            report.append("----------------------------------------\n");
        }

        return report.toString();
    }

    public String generateCleanerReport() {
        StringBuilder report = new StringBuilder();

        List<Cleaner> cleaners =
                cleanerDAO.getAllCleaners();

        report.append("CLEANER REPORT\n");
        report.append("==============\n\n");

        report.append("Generated: ")
                .append(DateHelper.getCurrentDateTime())
                .append("\n\n");

        if (cleaners.isEmpty()) {
            report.append("No cleaners are currently registered.\n");
            return report.toString();
        }

        for (Cleaner cleaner : cleaners) {
            int issuanceCount =
                    stockIssuanceDAO
                            .getIssuancesByCleaner(
                                    cleaner.getCleanerId()
                            )
                            .size();

            report.append("Cleaner ID: ")
                    .append(cleaner.getCleanerId())
                    .append("\n");

            report.append("Employee number: ")
                    .append(cleaner.getEmployeeNumber())
                    .append("\n");

            report.append("Name: ")
                    .append(cleaner.getFullName())
                    .append("\n");

            report.append("Phone number: ")
                    .append(cleaner.getPhoneNumber())
                    .append("\n");

            report.append("Email address: ")
                    .append(cleaner.getEmail())
                    .append("\n");

            report.append("Department: ")
                    .append(cleaner.getDepartment())
                    .append("\n");

            report.append("Status: ")
                    .append(
                            cleaner.isActive()
                                    ? "Active"
                                    : "Inactive"
                    )
                    .append("\n");

            report.append("Number of issuances: ")
                    .append(issuanceCount)
                    .append("\n");

            report.append("----------------------------------------\n");
        }

        return report.toString();
    }

    public String generateStockIssuanceReport() {
        StringBuilder report = new StringBuilder();

        List<StockIssuance> issuances =
                stockIssuanceDAO.getAllIssuances();

        report.append("STOCK ISSUANCE REPORT\n");
        report.append("=====================\n\n");

        report.append("Generated: ")
                .append(DateHelper.getCurrentDateTime())
                .append("\n\n");

        if (issuances.isEmpty()) {
            report.append(
                    "No stock issuance records are available.\n"
            );

            return report.toString();
        }

        for (StockIssuance issuance : issuances) {
            Material material = materialDAO.getMaterialById(
                    issuance.getMaterialId()
            );

            Cleaner cleaner = cleanerDAO.getCleanerById(
                    issuance.getCleanerId()
            );

            report.append("Issuance ID: ")
                    .append(issuance.getIssuanceId())
                    .append("\n");

            report.append("Material: ")
                    .append(
                            material == null
                                    ? "Unknown material"
                                    : material.getMaterialName()
                    )
                    .append("\n");

            report.append("Cleaner: ")
                    .append(
                            cleaner == null
                                    ? "Unknown cleaner"
                                    : cleaner.getFullName()
                    )
                    .append("\n");

            report.append("Quantity issued: ")
                    .append(issuance.getQuantityIssued())
                    .append("\n");

            report.append("Issued by user ID: ")
                    .append(issuance.getIssuedByUserId())
                    .append("\n");

            report.append("Date issued: ")
                    .append(
                            DateHelper.formatDateTime(
                                    issuance.getIssuanceDate()
                            )
                    )
                    .append("\n");

            report.append("Notes: ")
                    .append(
                            issuance.getNotes() == null
                                    || issuance.getNotes().isBlank()
                                    ? "None"
                                    : issuance.getNotes()
                    )
                    .append("\n");

            report.append("----------------------------------------\n");
        }

        report.append("\nTotal quantity issued: ")
                .append(stockIssuanceDAO.getTotalQuantityIssued())
                .append("\n");

        return report.toString();
    }

    public String generateIssuanceReportByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        StringBuilder report = new StringBuilder();

        List<StockIssuance> issuances =
                stockIssuanceDAO.getIssuancesByDateRange(
                        startDate,
                        endDate
                );

        report.append("STOCK ISSUANCE DATE-RANGE REPORT\n");
        report.append("===============================\n\n");

        report.append("From: ")
                .append(DateHelper.formatDateTime(startDate))
                .append("\n");

        report.append("To: ")
                .append(DateHelper.formatDateTime(endDate))
                .append("\n\n");

        if (issuances.isEmpty()) {
            report.append(
                    "No stock issuances were found in this period.\n"
            );

            return report.toString();
        }

        int totalQuantity = 0;

        for (StockIssuance issuance : issuances) {
            Material material = materialDAO.getMaterialById(
                    issuance.getMaterialId()
            );

            Cleaner cleaner = cleanerDAO.getCleanerById(
                    issuance.getCleanerId()
            );

            report.append("Issuance ID: ")
                    .append(issuance.getIssuanceId())
                    .append("\n");

            report.append("Material: ")
                    .append(
                            material == null
                                    ? "Unknown"
                                    : material.getMaterialName()
                    )
                    .append("\n");

            report.append("Cleaner: ")
                    .append(
                            cleaner == null
                                    ? "Unknown"
                                    : cleaner.getFullName()
                    )
                    .append("\n");

            report.append("Quantity: ")
                    .append(issuance.getQuantityIssued())
                    .append("\n");

            report.append("Date: ")
                    .append(
                            DateHelper.formatDateTime(
                                    issuance.getIssuanceDate()
                            )
                    )
                    .append("\n");

            report.append("----------------------------------------\n");

            totalQuantity += issuance.getQuantityIssued();
        }

        report.append("\nTotal records: ")
                .append(issuances.size())
                .append("\n");

        report.append("Total quantity issued: ")
                .append(totalQuantity)
                .append("\n");

        return report.toString();
    }
}