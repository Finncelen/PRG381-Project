package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import reports.ReportGenerator;

public class ReportsFrame extends JFrame {

    private final ReportGenerator reportGenerator;

    private JTextArea txtReport;

    public ReportsFrame() {
        reportGenerator = new ReportGenerator();

        initializeFrame();
        createComponents();
        showSummaryReport();
    }

    private void initializeFrame() {
        setTitle("Cleaning Inventory Reports");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void createComponents() {
        setLayout(new BorderLayout(10, 10));

        txtReport = new JTextArea();

        txtReport.setEditable(false);
        txtReport.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        13
                )
        );

        txtReport.setLineWrap(false);
        txtReport.setTabSize(4);

        JButton btnSummary =
                new JButton("Summary");

        JButton btnMaterials =
                new JButton("Materials");

        JButton btnLowStock =
                new JButton("Low Stock");

        JButton btnSuppliers =
                new JButton("Suppliers");

        JButton btnCleaners =
                new JButton("Cleaners");

        JButton btnIssuances =
                new JButton("Stock Issuances");

        JButton btnSave =
                new JButton("Save Report");

        JButton btnRefresh =
                new JButton("Refresh");

        JButton btnClose =
                new JButton("Close");

        JPanel reportButtonPanel = new JPanel(
                new GridLayout(2, 3, 8, 8)
        );

        reportButtonPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Select Report"
                )
        );

        reportButtonPanel.add(btnSummary);
        reportButtonPanel.add(btnMaterials);
        reportButtonPanel.add(btnLowStock);
        reportButtonPanel.add(btnSuppliers);
        reportButtonPanel.add(btnCleaners);
        reportButtonPanel.add(btnIssuances);

        JPanel actionButtonPanel = new JPanel();

        actionButtonPanel.add(btnSave);
        actionButtonPanel.add(btnRefresh);
        actionButtonPanel.add(btnClose);

        JPanel topPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        0,
                        10
                )
        );

        topPanel.add(
                reportButtonPanel,
                BorderLayout.CENTER
        );

        topPanel.add(
                actionButtonPanel,
                BorderLayout.SOUTH
        );

        JScrollPane reportScrollPane =
                new JScrollPane(txtReport);

        reportScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Generated Report"
                )
        );

        add(topPanel, BorderLayout.NORTH);
        add(reportScrollPane, BorderLayout.CENTER);

        btnSummary.addActionListener(event ->
                showSummaryReport()
        );

        btnMaterials.addActionListener(event ->
                showMaterialsReport()
        );

        btnLowStock.addActionListener(event ->
                showLowStockReport()
        );

        btnSuppliers.addActionListener(event ->
                showSupplierReport()
        );

        btnCleaners.addActionListener(event ->
                showCleanerReport()
        );

        btnIssuances.addActionListener(event ->
                showIssuanceReport()
        );

        btnSave.addActionListener(event ->
                saveReportToFile()
        );

        btnRefresh.addActionListener(event ->
                showSummaryReport()
        );

        btnClose.addActionListener(event ->
                dispose()
        );
    }

    private void showSummaryReport() {
        displayReport(
                reportGenerator.generateSummaryReport()
        );
    }

    private void showMaterialsReport() {
        displayReport(
                reportGenerator.generateMaterialsReport()
        );
    }

    private void showLowStockReport() {
        displayReport(
                reportGenerator.generateLowStockReport()
        );
    }

    private void showSupplierReport() {
        displayReport(
                reportGenerator.generateSupplierReport()
        );
    }

    private void showCleanerReport() {
        displayReport(
                reportGenerator.generateCleanerReport()
        );
    }

    private void showIssuanceReport() {
        displayReport(
                reportGenerator
                        .generateStockIssuanceReport()
        );
    }

    private void displayReport(String reportText) {
        if (reportText == null
                || reportText.trim().isEmpty()) {

            txtReport.setText(
                    "The selected report contains no information."
            );
        } else {
            txtReport.setText(reportText);
        }

        txtReport.setCaretPosition(0);
    }

    private void saveReportToFile() {
        String reportText = txtReport.getText();

        if (reportText == null
                || reportText.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Generate a report before saving.",
                    "No Report",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setDialogTitle(
                "Save Inventory Report"
        );

        fileChooser.setSelectedFile(
                new File("inventory-report.txt")
        );

        fileChooser.setFileFilter(
                new FileNameExtensionFilter(
                        "Text Documents (*.txt)",
                        "txt"
                )
        );

        int result =
                fileChooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile =
                fileChooser.getSelectedFile();

        if (!selectedFile.getName()
                .toLowerCase()
                .endsWith(".txt")) {

            selectedFile = new File(
                    selectedFile.getAbsolutePath()
                    + ".txt"
            );
        }

        if (selectedFile.exists()) {
            int overwriteChoice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "The file already exists. "
                            + "Replace it?",
                            "Confirm Replace",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (overwriteChoice
                    != JOptionPane.YES_OPTION) {

                return;
            }
        }

        try (FileWriter writer =
                new FileWriter(selectedFile)) {

            writer.write(reportText);

            JOptionPane.showMessageDialog(
                    this,
                    "Report saved successfully:\n"
                    + selectedFile.getAbsolutePath(),
                    "Report Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "The report could not be saved.\n\n"
                    + exception.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            ReportsFrame frame =
                    new ReportsFrame();

            frame.setVisible(true);
        });
    }
}