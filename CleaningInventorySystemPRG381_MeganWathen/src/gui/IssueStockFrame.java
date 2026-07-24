package gui;

import dao.CleanerDAO;
import dao.MaterialDAO;
import dao.StockIssuanceDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import models.Cleaner;
import models.Material;
import models.StockIssuance;
import utils.DateHelper;
import validation.Validation;

public class IssueStockFrame extends JFrame {

    private final MaterialDAO materialDAO;
    private final CleanerDAO cleanerDAO;
    private final StockIssuanceDAO issuanceDAO;

    private JComboBox<Material> cmbMaterial;
    private JComboBox<Cleaner> cmbCleaner;

    private JTextField txtQuantity;
    private JTextArea txtNotes;
    private JLabel lblAvailableStock;

    private JTable issuanceTable;
    private DefaultTableModel tableModel;

    public IssueStockFrame() {
        materialDAO = new MaterialDAO();
        cleanerDAO = new CleanerDAO();
        issuanceDAO = new StockIssuanceDAO();

        initializeFrame();
        createComponents();
        loadMaterials();
        loadCleaners();
        loadIssuances();
        updateAvailableStock();
    }

    private void initializeFrame() {
        setTitle("Issue Cleaning Stock");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void createComponents() {
        setLayout(new BorderLayout(10, 10));

        cmbMaterial = new JComboBox<>();
        cmbCleaner = new JComboBox<>();

        txtQuantity = new JTextField();

        txtNotes = new JTextArea(3, 20);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);

        lblAvailableStock = new JLabel("0");

        JPanel formPanel = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Stock Issuance Details"
                )
        );

        formPanel.add(new JLabel("Material:"));
        formPanel.add(cmbMaterial);

        formPanel.add(new JLabel("Available Stock:"));
        formPanel.add(lblAvailableStock);

        formPanel.add(new JLabel("Cleaner:"));
        formPanel.add(cmbCleaner);

        formPanel.add(new JLabel("Quantity to Issue:"));
        formPanel.add(txtQuantity);

        formPanel.add(new JLabel("Notes:"));
        formPanel.add(new JScrollPane(txtNotes));

        JButton btnIssue = new JButton("Issue Stock");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnDelete = new JButton("Delete Selected Record");
        JButton btnClear = new JButton("Clear");
        JButton btnClose = new JButton("Close");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnIssue);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnClose);

        tableModel = new DefaultTableModel(
                new String[]{
                    "Issuance ID",
                    "Material",
                    "Cleaner",
                    "Quantity",
                    "Date",
                    "Notes"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        issuanceTable = new JTable(tableModel);

        issuanceTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        issuanceTable.setAutoCreateRowSorter(true);

        JPanel topPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        5,
                        15
                )
        );

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane tableScrollPane =
                new JScrollPane(issuanceTable);

        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Stock Issuance History"
                )
        );

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        cmbMaterial.addActionListener(event ->
                updateAvailableStock()
        );

        btnIssue.addActionListener(event ->
                issueStock()
        );

        btnRefresh.addActionListener(event ->
                refreshData()
        );

        btnDelete.addActionListener(event ->
                deleteSelectedIssuance()
        );

        btnClear.addActionListener(event ->
                clearFields()
        );

        btnClose.addActionListener(event ->
                dispose()
        );
    }

    private void loadMaterials() {
        Material previouslySelected =
                (Material) cmbMaterial.getSelectedItem();

        int selectedMaterialId = previouslySelected == null
                ? -1
                : previouslySelected.getMaterialId();

        cmbMaterial.removeAllItems();

        for (Material material :
                materialDAO.getAllMaterials()) {

            cmbMaterial.addItem(material);
        }

        if (selectedMaterialId != -1) {
            selectMaterial(selectedMaterialId);
        }
    }

    private void selectMaterial(int materialId) {
        for (int i = 0; i < cmbMaterial.getItemCount(); i++) {
            Material material = cmbMaterial.getItemAt(i);

            if (material.getMaterialId() == materialId) {
                cmbMaterial.setSelectedIndex(i);
                return;
            }
        }
    }

    private void loadCleaners() {
        Cleaner previouslySelected =
                (Cleaner) cmbCleaner.getSelectedItem();

        int selectedCleanerId = previouslySelected == null
                ? -1
                : previouslySelected.getCleanerId();

        cmbCleaner.removeAllItems();

        for (Cleaner cleaner :
                cleanerDAO.getAllCleaners()) {

            if (cleaner.isActive()) {
                cmbCleaner.addItem(cleaner);
            }
        }

        if (selectedCleanerId != -1) {
            selectCleaner(selectedCleanerId);
        }
    }

    private void selectCleaner(int cleanerId) {
        for (int i = 0; i < cmbCleaner.getItemCount(); i++) {
            Cleaner cleaner = cmbCleaner.getItemAt(i);

            if (cleaner.getCleanerId() == cleanerId) {
                cmbCleaner.setSelectedIndex(i);
                return;
            }
        }
    }

    private void updateAvailableStock() {
        Material selectedMaterial =
                (Material) cmbMaterial.getSelectedItem();

        if (selectedMaterial == null) {
            lblAvailableStock.setText("0");
            return;
        }

        lblAvailableStock.setText(
                selectedMaterial.getQuantityInStock()
                + " "
                + selectedMaterial.getUnitOfMeasure()
        );
    }

    private void issueStock() {
        Material material =
                (Material) cmbMaterial.getSelectedItem();

        Cleaner cleaner =
                (Cleaner) cmbCleaner.getSelectedItem();

        if (material == null) {
            showError(
                    "No material is available. "
                    + "Add a material first."
            );
            return;
        }

        if (cleaner == null) {
            showError(
                    "No active cleaner is available. "
                    + "Add or activate a cleaner first."
            );
            return;
        }

        String quantityText =
                txtQuantity.getText().trim();

        if (!Validation.isPositiveInteger(quantityText)) {
            showError(
                    "Quantity must be a whole number "
                    + "greater than zero."
            );
            return;
        }

        int quantity;

        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException exception) {
            showError("The quantity is too large or invalid.");
            return;
        }

        if (!Validation.hasEnoughStock(
                material.getQuantityInStock(),
                quantity
        )) {
            showError(
                    """
                    Insufficient stock.
                    
                    Available: """
                    + material.getQuantityInStock()
                    + "\nRequested: "
                    + quantity
            );
            return;
        }

        StockIssuance issuance = new StockIssuance(
                0,
                material.getMaterialId(),
                cleaner.getCleanerId(),
                1,
                quantity,
                LocalDateTime.now(),
                txtNotes.getText().trim()
        );

        boolean successful;

        try {
            successful =
                    issuanceDAO.addIssuance(issuance);
        } catch (Exception exception) {
            showError(
                    "An unexpected error occurred:\n"
                    + exception.getMessage()
            );
            return;
        }

        if (successful) {
            JOptionPane.showMessageDialog(
                    this,
                    """
                    Stock issued successfully.
                    
                    Material: """
                    + material.getMaterialName()
                    + "\nCleaner: "
                    + cleaner.getFullName()
                    + "\nQuantity: "
                    + quantity,
                    "Stock Issued",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
            refreshData();
        } else {
            showError(
                    "Stock could not be issued. "
                    + "Check that enough stock is available."
            );
        }
    }

    private void loadIssuances() {
        tableModel.setRowCount(0);

        for (StockIssuance issuance :
                issuanceDAO.getAllIssuances()) {

            Material material =
                    materialDAO.getMaterialById(
                            issuance.getMaterialId()
                    );

            Cleaner cleaner =
                    cleanerDAO.getCleanerById(
                            issuance.getCleanerId()
                    );

            String materialName =
                    material == null
                            ? "Unknown material"
                            : material.getMaterialName();

            String cleanerName =
                    cleaner == null
                            ? "Unknown cleaner"
                            : cleaner.getFullName();

            tableModel.addRow(new Object[]{
                issuance.getIssuanceId(),
                materialName,
                cleanerName,
                issuance.getQuantityIssued(),
                DateHelper.formatDateTime(
                        issuance.getIssuanceDate()
                ),
                issuance.getNotes() == null
                        ? ""
                        : issuance.getNotes()
            });
        }
    }

    private void deleteSelectedIssuance() {
        int selectedViewRow =
                issuanceTable.getSelectedRow();

        if (selectedViewRow == -1) {
            showError(
                    "Select an issuance record to delete."
            );
            return;
        }

        int selectedModelRow =
                issuanceTable.convertRowIndexToModel(
                        selectedViewRow
                );

        int issuanceId = Integer.parseInt(
                tableModel.getValueAt(
                        selectedModelRow,
                        0
                ).toString()
        );

        int choice = JOptionPane.showConfirmDialog(this, """
                                                         Delete the selected issuance record?
                                                         
                                                         Note: deleting the history record does not restore the stock.""",
                "Delete Issuance",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (issuanceDAO.deleteIssuance(issuanceId)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Issuance record deleted successfully.",
                    "Record Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadIssuances();
        } else {
            showError(
                    "The issuance record could not be deleted."
            );
        }
    }

    private void refreshData() {
        loadMaterials();
        loadCleaners();
        loadIssuances();
        updateAvailableStock();
    }

    private void clearFields() {
        txtQuantity.setText("");
        txtNotes.setText("");
        issuanceTable.clearSelection();

        if (cmbMaterial.getItemCount() > 0) {
            cmbMaterial.setSelectedIndex(0);
        }

        if (cmbCleaner.getItemCount() > 0) {
            cmbCleaner.setSelectedIndex(0);
        }

        updateAvailableStock();
        txtQuantity.requestFocus();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Stock Issuance Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            IssueStockFrame frame =
                    new IssueStockFrame();

            frame.setVisible(true);
        });
    }
}