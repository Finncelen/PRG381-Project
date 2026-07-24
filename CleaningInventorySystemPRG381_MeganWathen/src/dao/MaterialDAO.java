package dao;

import java.util.ArrayList;
import java.util.List;
import models.Material;
import utils.IDGenerator;

public class MaterialDAO {

    private static final List<Material> materials = new ArrayList<>();

    public boolean addMaterial(Material material) {
        if (material == null
                || materialNameExists(material.getMaterialName())) {
            return false;
        }

        material.setMaterialId(IDGenerator.nextMaterialID());
        materials.add(material);
        return true;
    }

    public boolean updateMaterial(Material updatedMaterial) {
        for (int i = 0; i < materials.size(); i++) {
            if (materials.get(i).getMaterialId()
                    == updatedMaterial.getMaterialId()) {

                materials.set(i, updatedMaterial);
                return true;
            }
        }

        return false;
    }

    public boolean deleteMaterial(int materialId) {
        return materials.removeIf(
                material -> material.getMaterialId() == materialId
        );
    }

    public Material getMaterialById(int materialId) {
        for (Material material : materials) {
            if (material.getMaterialId() == materialId) {
                return material;
            }
        }

        return null;
    }

    public List<Material> getAllMaterials() {
        return new ArrayList<>(materials);
    }

    public List<Material> getLowStockMaterials() {
        List<Material> results = new ArrayList<>();

        for (Material material : materials) {
            if (material.isLowStock()) {
                results.add(material);
            }
        }

        return results;
    }

    public List<Material> searchMaterials(String searchText) {
        List<Material> results = new ArrayList<>();

        if (searchText == null) {
            return results;
        }

        String search = searchText.trim().toLowerCase();

        for (Material material : materials) {
            if (material.getMaterialName().toLowerCase().contains(search)
                    || material.getDescription()
                            .toLowerCase().contains(search)
                    || material.getUnitOfMeasure()
                            .toLowerCase().contains(search)) {

                results.add(material);
            }
        }

        return results;
    }

    public boolean materialNameExists(String materialName) {
        if (materialName == null) {
            return false;
        }

        for (Material material : materials) {
            if (material.getMaterialName()
                    .equalsIgnoreCase(materialName.trim())) {
                return true;
            }
        }

        return false;
    }

    public boolean issueStock(int materialId, int quantity) {
        Material material = getMaterialById(materialId);

        if (material == null || quantity <= 0) {
            return false;
        }

        try {
            material.deductStock(quantity);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}