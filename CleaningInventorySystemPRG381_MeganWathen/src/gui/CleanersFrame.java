package gui;

import dao.CleanerDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import models.Cleaner;
import validation.Validation;

public class CleanersFrame extends JFrame {

    private final CleanerDAO cleanerDAO;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmployeeNumber;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtDepartment;
    private JTextField txtSearch;

    private JCheckBox chkActive;

    private JTable cleanerTable;
    private DefaultTableModel tableModel;

    private int selectedCleanerId = -1;

    public CleanersFrame() {
        cleanerDAO = new CleanerDAO();

        initializeFrame();
        createComponents();
        loadCleaners();
    }

    private void initializeFrame() {
        setTitle("Manage Cleaners");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtEmployeeNumber = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtDepartment = new JTextField();

        chkActive = new JCheckBox("Active", true);

        formPanel.add(new JLabel("First Name:"));
        formPanel.add(txtFirstName);

        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(txtLastName);

        formPanel.add(new JLabel("Employee Number:"));
        formPanel.add(txtEmployeeNumber);

        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Email Address:"));
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Department/Location:"));
        formPanel.add(txtDepartment);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(chkActive);

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
                    "First Name",
                    "Last Name",
                    "Employee Number",
                    "Phone",
                    "Email",
                    "Department",
                    "Active"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cleanerTable = new JTable(tableModel);
        cleanerTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        cleanerTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                populateSelectedCleaner();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        topPanel.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(15, 15, 10, 15)
        );

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(cleanerTable), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(event -> addCleaner());
        btnUpdate.addActionListener(event -> updateCleaner());
        btnDelete.addActionListener(event -> deleteCleaner());
        btnClear.addActionListener(event -> clearFields());
        btnSearch.addActionListener(event -> searchCleaners());
        btnShowAll.addActionListener(event -> loadCleaners());
    }

    private void addCleaner() {
        if (!validateFields()) {
            return;
        }

        Cleaner cleaner = createCleanerFromFields(0);

        if (cleanerDAO.addCleaner(cleaner)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cleaner added successfully."
            );

            clearFields();
            loadCleaners();
        } else {
            showError(
                    "Cleaner could not be added. "
                    + "The employee number may already exist."
            );
        }
    }

    private void updateCleaner() {
        if (selectedCleanerId == -1) {
            showError("Select a cleaner to update.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        Cleaner cleaner = createCleanerFromFields(
                selectedCleanerId
        );

        if (cleanerDAO.updateCleaner(cleaner)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cleaner updated successfully."
            );

            clearFields();
            loadCleaners();
        } else {
            showError("Cleaner could not be updated.");
        }
    }

    private void deleteCleaner() {
        if (selectedCleanerId == -1) {
            showError("Select a cleaner to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this cleaner?",
                "Delete Cleaner",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            if (cleanerDAO.deleteCleaner(selectedCleanerId)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cleaner deleted successfully."
                );

                clearFields();
                loadCleaners();
            } else {
                showError("Cleaner could not be deleted.");
            }
        }
    }

    private Cleaner createCleanerFromFields(int cleanerId) {
        return new Cleaner(
                cleanerId,
                txtFirstName.getText().trim(),
                txtLastName.getText().trim(),
                txtEmployeeNumber.getText().trim(),
                txtPhone.getText().trim(),
                txtEmail.getText().trim(),
                txtDepartment.getText().trim(),
                chkActive.isSelected()
        );
    }

    private boolean validateFields() {
        if (Validation.isEmpty(txtFirstName.getText())
                || Validation.isEmpty(txtLastName.getText())
                || Validation.isEmpty(txtEmployeeNumber.getText())
                || Validation.isEmpty(txtPhone.getText())
                || Validation.isEmpty(txtEmail.getText())
                || Validation.isEmpty(txtDepartment.getText())) {

            showError("Please complete all cleaner fields.");
            return false;
        }

        if (!Validation.isValidName(txtFirstName.getText())
                || !Validation.isValidName(txtLastName.getText())) {

            showError("Enter valid first and last names.");
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

    private void loadCleaners() {
        displayCleaners(cleanerDAO.getAllCleaners());
    }

    private void searchCleaners() {
        String searchText = txtSearch.getText().trim();

        if (searchText.isEmpty()) {
            loadCleaners();
            return;
        }

        displayCleaners(
                cleanerDAO.searchCleaners(searchText)
        );
    }

    private void displayCleaners(List<Cleaner> cleaners) {
        tableModel.setRowCount(0);

        for (Cleaner cleaner : cleaners) {
            tableModel.addRow(new Object[]{
                cleaner.getCleanerId(),
                cleaner.getFirstName(),
                cleaner.getLastName(),
                cleaner.getEmployeeNumber(),
                cleaner.getPhoneNumber(),
                cleaner.getEmail(),
                cleaner.getDepartment(),
                cleaner.isActive()
            });
        }
    }

    private void populateSelectedCleaner() {
        int selectedRow = cleanerTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        selectedCleanerId = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString()
        );

        txtFirstName.setText(
                tableModel.getValueAt(selectedRow, 1).toString()
        );

        txtLastName.setText(
                tableModel.getValueAt(selectedRow, 2).toString()
        );

        txtEmployeeNumber.setText(
                tableModel.getValueAt(selectedRow, 3).toString()
        );

        txtPhone.setText(
                tableModel.getValueAt(selectedRow, 4).toString()
        );

        txtEmail.setText(
                tableModel.getValueAt(selectedRow, 5).toString()
        );

        txtDepartment.setText(
                tableModel.getValueAt(selectedRow, 6).toString()
        );

        chkActive.setSelected(
                Boolean.parseBoolean(
                        tableModel.getValueAt(selectedRow, 7).toString()
                )
        );
    }

    private void clearFields() {
        selectedCleanerId = -1;

        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmployeeNumber.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtDepartment.setText("");

        chkActive.setSelected(true);
        cleanerTable.clearSelection();
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
  