package dao;

import java.util.ArrayList;
import java.util.List;
import models.Supplier;
import utils.IDGenerator;

public class SupplierDAO {

    private static final List<Supplier> suppliers = new ArrayList<>();

    public boolean addSupplier(Supplier supplier) {
        if (supplier == null || supplierNameExists(supplier.getSupplierName())) {
            return false;
        }

        supplier.setSupplierId(IDGenerator.nextSupplierID());
        suppliers.add(supplier);
        return true;
    }

    public boolean updateSupplier(Supplier updatedSupplier) {
        for (int i = 0; i < suppliers.size(); i++) {
            if (suppliers.get(i).getSupplierId()
                    == updatedSupplier.getSupplierId()) {

                suppliers.set(i, updatedSupplier);
                return true;
            }
        }

        return false;
    }

    public boolean deleteSupplier(int supplierId) {
        return suppliers.removeIf(
                supplier -> supplier.getSupplierId() == supplierId
        );
    }

    public Supplier getSupplierById(int supplierId) {
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierId() == supplierId) {
                return supplier;
            }
        }

        return null;
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers);
    }

    public List<Supplier> searchSuppliers(String searchText) {
        List<Supplier> results = new ArrayList<>();

        if (searchText == null) {
            return results;
        }

        String search = searchText.trim().toLowerCase();

        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierName().toLowerCase().contains(search)
                    || supplier.getContactPerson().toLowerCase().contains(search)
                    || supplier.getEmail().toLowerCase().contains(search)) {

                results.add(supplier);
            }
        }

        return results;
    }

    public boolean supplierNameExists(String supplierName) {
        if (supplierName == null) {
            return false;
        }

        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierName()
                    .equalsIgnoreCase(supplierName.trim())) {
                return true;
            }
        }

        return false;
    }
}