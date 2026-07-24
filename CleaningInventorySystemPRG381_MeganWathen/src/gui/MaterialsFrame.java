package gui;

import dao.MaterialDAO;
import dao.SupplierDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import models.Material;
import models.Supplier;
import validation.Validation;

public class MaterialsFrame extends JFrame {

    private final MaterialDAO materialDAO;
    private final SupplierDAO supplierDAO;

    private JTextField txtName;
    private JTextField txtDescription;
    private JTextField txtQuantity;
    private JTextField txtReorderLevel;
    private JTextField txtUnit;
    private JTextField txtPrice;
    private JTextField txtSearch;

    private JComboBox<Supplier> cmbSupplier;

    private JTable materialTable;
    private DefaultTableModel tableModel;

    private int selectedMaterialId = -1;

    public MaterialsFrame() {
        materialDAO = new MaterialDAO();
        supplierDAO = new SupplierDAO();

        initializeFrame();
        createComponents();
        loadSuppliers();
        loadMaterials();
    }

    private void initializeFrame() {
        setTitle("Manage Cleaning Materials");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        txtName = new JTextField();
        txtDescription = new JTextField();
        txtQuantity = new JTextField();
        txtReorderLevel = new JTextField();
        txtUnit = new JTextField();
        txtPrice = new JTextField();

        cmbSupplier = new JComboBox<>();

        formPanel.add(new JLabel("Material Name:"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Description:"));
        formPanel.add(txtDescription);

        formPanel.add(new JLabel("Quantity in Stock:"));
        formPanel.add(txtQuantity);

        formPanel.add(new JLabel("Reorder Level:"));
        formPanel.add(txtReorderLevel);

        formPanel.add(new JLabel("Unit of Measure:"));
        formPanel.add(txtUnit);

        formPanel.add(new JLabel("Unit Price:"));
        formPanel.add(txtPrice);

        formPanel.add(new JLabel("Supplier:"));
        formPanel.add(cmbSupplier);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        txtSearch = new JTextField(20);

        JButton btnSearch = new JButton("Search");
        JButton btnShowAll = new JButton("Show All");
        JButton btnLowStock = new JButton("Low Stock");

        JPanel searchPanel = new JPanel();

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);
        searchPanel.add(btnLowStock);

        tableModel = new DefaultTableModel(
                new String[]{
                    "ID",
                    "Name",
                    "Description",
                    "Stock",
                    "Reorder Level",
                    "Unit",
                    "Price",
                    "Supplier ID",
                    "Low Stock"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        materialTable = new JTable(tableModel);
        materialTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        materialTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                populateSelectedMaterial();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        topPanel.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(15, 15, 10, 15)
        );

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(materialTable), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(event -> addMaterial());
        btnUpdate.addActionListener(event -> updateMaterial());
        btnDelete.addActionListener(event -> deleteMaterial());
        btnClear.addActionListener(event -> clearFields());
        btnSearch.addActionListener(event -> searchMaterials());
        btnShowAll.addActionListener(event -> loadMaterials());
        btnLowStock.addActionListener(event ->
                displayMaterials(materialDAO.getLowStockMaterials())
        );
    }

    private void loadSuppliers() {
        cmbSupplier.removeAllItems();

        for (Supplier supplier : supplierDAO.getAllSuppliers()) {
            cmbSupplier.addItem(supplier);
        }
    }

    private void addMaterial() {
        if (!validateFields()) {
            return;
        }

        Supplier supplier = (Supplier) cmbSupplier.getSelectedItem();

        Material material = new Material(
                0,
                txtName.getText().trim(),
                txtDescription.getText().trim(),
                Integer.parseInt(txtQuantity.getText().trim()),
                Integer.parseInt(txtReorderLevel.getText().trim()),
                txtUnit.getText().trim(),
                Double.parseDouble(txtPrice.getText().trim()),
                supplier.getSupplierId()
        );

        if (materialDAO.addMaterial(material)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Material added successfully."
            );

            clearFields();
            loadMaterials();
        } else {
            showError(
                    "Material could not be added. "
                    + "The name may already exist."
            );
        }
    }

    private void updateMaterial() {
        if (selectedMaterialId == -1) {
            showError("Select a material to update.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        Supplier supplier = (Supplier) cmbSupplier.getSelectedItem();

        Material material = new Material(
                selectedMaterialId,
                txtName.getText().trim(),
                txtDescription.getText().trim(),
                Integer.parseInt(txtQuantity.getText().trim()),
                Integer.parseInt(txtReorderLevel.getText().trim()),
                txtUnit.getText().trim(),
                Double.parseDouble(txtPrice.getText().trim()),
                supplier.getSupplierId()
        );

        if (materialDAO.updateMaterial(material)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Material updated successfully."
            );

            clearFields();
            loadMaterials();
        } else {
            showError("Material could not be updated.");
        }
    }

    private void deleteMaterial() {
        if (selectedMaterialId == -1) {
            showError("Select a material to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this material?",
                "Delete Material",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            if (materialDAO.deleteMaterial(selectedMaterialId)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Material deleted successfully."
                );

                clearFields();
                loadMaterials();
            } else {
                showError("Material could not be deleted.");
            }
        }
    }

    private boolean validateFields() {
        if (Validation.isEmpty(txtName.getText())
                || Validation.isEmpty(txtDescription.getText())
                || Validation.isEmpty(txtQuantity.getText())
                || Validation.isEmpty(txtReorderLevel.getText())
                || Validation.isEmpty(txtUnit.getText())
                || Validation.isEmpty(txtPrice.getText())) {

            showError("Please complete all material fields.");
            return false;
        }

        if (!Validation.isNonNegativeInteger(txtQuantity.getText())) {
            showError("Stock quantity must be zero or greater.");
            return false;
        }

        if (!Validation.isNonNegativeInteger(txtReorderLevel.getText())) {
            showError("Reorder level must be zero or greater.");
            return false;
        }

        if (!Validation.isNonNegativeDouble(txtPrice.getText())) {
            showError("Unit price must be zero or greater.");
            return false;
        }

        if (cmbSupplier.getSelectedItem() == null) {
            showError(
                    "Add at least one supplier before adding a material."
            );

            return false;
        }

        return true;
    }

    private void loadMaterials() {
        displayMaterials(materialDAO.getAllMaterials());
    }

    private void searchMaterials() {
        String searchText = txtSearch.getText().trim();

        if (searchText.isEmpty()) {
            loadMaterials();
            return;
        }

        displayMaterials(
                materialDAO.searchMaterials(searchText)
        );
    }

    private void displayMaterials(List<Material> materials) {
        tableModel.setRowCount(0);

        for (Material material : materials) {
            tableModel.addRow(new Object[]{
                material.getMaterialId(),
                material.getMaterialName(),
                material.getDescription(),
                material.getQuantityInStock(),
                material.getReorderLevel(),
                material.getUnitOfMeasure(),
                material.getUnitPrice(),
                material.getSupplierId(),
                material.isLowStock()
            });
        }
    }

    private void populateSelectedMaterial() {
        int selectedRow = materialTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        selectedMaterialId = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString()
        );

        Material material = materialDAO.getMaterialById(
                selectedMaterialId
        );

        if (material == null) {
            return;
        }

        txtName.setText(material.getMaterialName());
        txtDescription.setText(material.getDescription());

        txtQuantity.setText(
                String.valueOf(material.getQuantityInStock())
        );

        txtReorderLevel.setText(
                String.valueOf(material.getReorderLevel())
        );

        txtUnit.setText(material.getUnitOfMeasure());

        txtPrice.setText(
                String.valueOf(material.getUnitPrice())
        );

        selectSupplier(material.getSupplierId());
    }

    private void selectSupplier(int supplierId) {
        for (int i = 0; i < cmbSupplier.getItemCount(); i++) {
            Supplier supplier = cmbSupplier.getItemAt(i);

            if (supplier.getSupplierId() == supplierId) {
                cmbSupplier.setSelectedIndex(i);
                break;
            }
        }
    }

    private void clearFields() {
        selectedMaterialId = -1;

        txtName.setText("");
        txtDescription.setText("");
        txtQuantity.setText("");
        txtReorderLevel.setText("");
        txtUnit.setText("");
        txtPrice.setText("");

        if (cmbSupplier.getItemCount() > 0) {
            cmbSupplier.setSelectedIndex(0);
        }

        materialTable.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}