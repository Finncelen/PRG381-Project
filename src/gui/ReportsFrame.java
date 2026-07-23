package gui;

import dao.CleanerDAO;
import dao.MaterialDAO;
import dao.StockIssuanceDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
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
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        cmbMaterial = new JComboBox<>();
        cmbCleaner = new JComboBox<>();

        txtQuantity = new JTextField();
        txtNotes = new JTextArea(3, 20);

        lblAvailableStock = new JLabel("Available Stock: 0");

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

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

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnIssue);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDelete);

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
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        issuanceTable = new JTable(tableModel);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        topPanel.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(15, 15, 10, 15)
        );

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(issuanceTable), BorderLayout.CENTER);

        cmbMaterial.addActionListener(event ->
                updateAvailableStock()
        );

        btnIssue.addActionListener(event -> issueStock());

        btnRefresh.addActionListener(event -> {
            loadMaterials();
            loadCleaners();
            loadIssuances();
            updateAvailableStock();
        });

        btnDelete.addActionListener(event ->
                deleteSelectedIssuance()
        );
    }

    private void loadMaterials() {
        cmbMaterial.removeAllItems();

        for (Material material : materialDAO.getAllMaterials()) {
            cmbMaterial.addItem(material);
        }
    }

    private void loadCleaners() {
        cmbCleaner.removeAllItems();

        for (Cleaner cleaner : cleanerDAO.getAllCleaners()) {
            if (cleaner.isActive()) {
                cmbCleaner.addItem(cleaner);
            }
        }
    }

    private void updateAvailableStock() {
        Material selectedMaterial =
                (Material) cmbMaterial.getSelectedItem();

        if (selectedMaterial == null) {
            lblAvailableStock.setText("0");
        } else {
            lblAvailableStock.setText(
                    String.valueOf(
                            selectedMaterial.getQuantityInStock()
                    )
            );
        }
    }

    private void issueStock() {
        Material material =
                (Material) cmbMaterial.getSelectedItem();

        Cleaner cleaner =
                (Cleaner) cmbCleaner.getSelectedItem();

        if (material == null) {
            showError("No material is available.");
            return;
        }

        if (cleaner == null) {
            showError("No active cleaner is available.");
            return;
        }

        if (!Validation.isPositiveInteger(txtQuantity.getText())) {
            showError(
                    "Quantity must be a whole number greater than zero."
            );

            return;
        }

        int quantity = Integer.parseInt(
                txtQuantity.getText().trim()
        );

        if (!Validation.hasEnoughStock(
                material.getQuantityInStock(),
                quantity
        )) {
            showError(
                    "There is not enough stock available."
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

        if (issuanceDAO.addIssuance(issuance)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Stock issued successfully."
            );

            txtQuantity.setText("");
            txtNotes.setText("");

            updateAvailableStock();
            loadIssuances();
        } else {
            showError("Stock could not be issued.");
        }
    }

    private void loadIssuances() {
        tableModel.setRowCount(0);

        for (StockIssuance issuance :
                issuanceDAO.getAllIssuances()) {

            Material material = materialDAO.getMaterialById(
                    issuance.getMaterialId()
            );

            Cleaner cleaner = cleanerDAO.getCleanerById(
                    issuance.getCleanerId()
            );

            String materialName = material == null
                    ? "Unknown"
                    : material.getMaterialName();

            String cleanerName = cleaner == null
                    ? "Unknown"
                    : cleaner.getFullName();

            tableModel.addRow(new Object[]{
                issuance.getIssuanceId(),
                materialName,
                cleanerName,
                issuance.getQuantityIssued(),
                DateHelper.formatDateTime(
                        issuance.getIssuanceDate()
                ),
                issuance.getNotes()
            });
        }
    }

    private void deleteSelectedIssuance() {
        int selectedRow = issuanceTable.getSelectedRow();

        if (selectedRow == -1) {
            showError("Select an issuance record to delete.");
            return;
        }

        int issuanceId = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString()
        );

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete the selected issuance record?",
                "Delete Issuance",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            if (issuanceDAO.deleteIssuance(issuanceId)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Issuance record deleted."
                );

                loadIssuances();
            } else {
                showError("The record could not be deleted.");
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Stock Issuance Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
