package gui;

import dao.SupplierDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import models.Supplier;
import validation.Validation;

public class SuppliersFrame extends JFrame {

    private final SupplierDAO supplierDAO;

    private JTextField txtName;
    private JTextField txtContactPerson;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtSearch;

    private JTable supplierTable;
    private DefaultTableModel tableModel;

    private int selectedSupplierId = -1;

    public SuppliersFrame() {
        supplierDAO = new SupplierDAO();

        initializeFrame();
        createComponents();
        loadSuppliers();
    }

    private void initializeFrame() {
        setTitle("Manage Suppliers");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        txtName = new JTextField();
        txtContactPerson = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();

        formPanel.add(new JLabel("Supplier Name:"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Contact Person:"));
        formPanel.add(txtContactPerson);

        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Email Address:"));
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Address:"));
        formPanel.add(txtAddress);

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

        JPanel searchPanel = new JPanel();

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        tableModel = new DefaultTableModel(
                new String[]{
                    "ID",
                    "Supplier Name",
                    "Contact Person",
                    "Phone",
                    "Email",
                    "Address"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        supplierTable = new JTable(tableModel);
        supplierTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        supplierTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                populateSelectedSupplier();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        topPanel.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(15, 15, 10, 15)
        );

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(supplierTable), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(event -> addSupplier());
        btnUpdate.addActionListener(event -> updateSupplier());
        btnDelete.addActionListener(event -> deleteSupplier());
        btnClear.addActionListener(event -> clearFields());
        btnSearch.addActionListener(event -> searchSuppliers());
        btnShowAll.addActionListener(event -> loadSuppliers());
    }

    private void addSupplier() {
        if (!validateFields()) {
            return;
        }

        Supplier supplier = new Supplier(
                0,
                txtName.getText().trim(),
                txtContactPerson.getText().trim(),
                txtPhone.getText().trim(),
                txtEmail.getText().trim(),
                txtAddress.getText().trim()
        );

        if (supplierDAO.addSupplier(supplier)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Supplier added successfully."
            );

            clearFields();
            loadSuppliers();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Supplier could not be added. The name may already exist.",
                    "Add Supplier",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateSupplier() {
        if (selectedSupplierId == -1) {
            showError("Select a supplier to update.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        Supplier supplier = new Supplier(
                selectedSupplierId,
                txtName.getText().trim(),
                txtContactPerson.getText().trim(),
                txtPhone.getText().trim(),
                txtEmail.getText().trim(),
                txtAddress.getText().trim()
        );

        if (supplierDAO.updateSupplier(supplier)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Supplier updated successfully."
            );

            clearFields();
            loadSuppliers();
        } else {
            showError("Supplier could not be updated.");
        }
    }

    private void deleteSupplier() {
        if (selectedSupplierId == -1) {
            showError("Select a supplier to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this supplier?",
                "Delete Supplier",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            if (supplierDAO.deleteSupplier(selectedSupplierId)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Supplier deleted successfully."
                );

                clearFields();
                loadSuppliers();
            } else {
                showError("Supplier could not be deleted.");
            }
        }
    }

    private boolean validateFields() {
        if (Validation.isEmpty(txtName.getText())
                || Validation.isEmpty(txtContactPerson.getText())
                || Validation.isEmpty(txtPhone.getText())
                || Validation.isEmpty(txtEmail.getText())
                || Validation.isEmpty(txtAddress.getText())) {

            showError("Please complete all supplier fields.");
            return false;
        }

        if (!Validation.isValidPhoneNumber(txtPhone.getText())) {
            showError("Enter a valid South African phone number.");
            return false;
        }

        if (!Validation.isValidEmail(txtEmail.getText())) {
            showError("Enter a valid email address.");
            return false;
        }

        return true;
    }

    private void loadSuppliers() {
        displaySuppliers(supplierDAO.getAllSuppliers());
    }

    private void searchSuppliers() {
        String searchText = txtSearch.getText().trim();

        if (searchText.isEmpty()) {
            loadSuppliers();
            return;
        }

        displaySuppliers(
                supplierDAO.searchSuppliers(searchText)
        );
    }

    private void displaySuppliers(List<Supplier> suppliers) {
        tableModel.setRowCount(0);

        for (Supplier supplier : suppliers) {
            tableModel.addRow(new Object[]{
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getContactPerson(),
                supplier.getPhoneNumber(),
                supplier.getEmail(),
                supplier.getAddress()
            });
        }
    }

    private void populateSelectedSupplier() {
        int selectedRow = supplierTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        selectedSupplierId = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString()
        );

        txtName.setText(
                tableModel.getValueAt(selectedRow, 1).toString()
        );

        txtContactPerson.setText(
                tableModel.getValueAt(selectedRow, 2).toString()
        );

        txtPhone.setText(
                tableModel.getValueAt(selectedRow, 3).toString()
        );

        txtEmail.setText(
                tableModel.getValueAt(selectedRow, 4).toString()
        );

        txtAddress.setText(
                tableModel.getValueAt(selectedRow, 5).toString()
        );
    }

    private void clearFields() {
        selectedSupplierId = -1;

        txtName.setText("");
        txtContactPerson.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");

        supplierTable.clearSelection();
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
