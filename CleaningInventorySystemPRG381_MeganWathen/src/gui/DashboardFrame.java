package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class DashboardFrame extends JFrame {

    private JButton btnSuppliers;
    private JButton btnCleaners;
    private JButton btnMaterials;
    private JButton btnIssueStock;
    private JButton btnReports;
    private JButton btnLogout;

    public DashboardFrame() {
        initialiseFrame();
        createComponents();
        addComponents();
        registerEvents();
    }

    private void initialiseFrame() {
        setTitle("Cleaning Inventory System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 520));
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createComponents() {
        btnSuppliers = new JButton("Manage Suppliers");
        btnCleaners = new JButton("Manage Cleaners");
        btnMaterials = new JButton("Manage Materials");
        btnIssueStock = new JButton("Issue Stock");
        btnReports = new JButton("View Reports");
        btnLogout = new JButton("Logout");

        Font buttonFont = new Font("Arial", Font.PLAIN, 17);

        btnSuppliers.setFont(buttonFont);
        btnCleaners.setFont(buttonFont);
        btnMaterials.setFont(buttonFont);
        btnIssueStock.setFont(buttonFont);
        btnReports.setFont(buttonFont);
        btnLogout.setFont(buttonFont);
    }

    private void addComponents() {
        setLayout(new BorderLayout(20, 20));

        JLabel lblTitle = new JLabel(
                "University Cleaning Inventory System",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setBorder(
                BorderFactory.createEmptyBorder(25, 10, 15, 10)
        );

        JLabel lblWelcome = new JLabel(
                "Select an option below",
                SwingConstants.CENTER
        );

        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(lblWelcome, BorderLayout.SOUTH);

        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 20, 20));

        menuPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 80, 30, 80)
        );

        menuPanel.add(btnSuppliers);
        menuPanel.add(btnCleaners);
        menuPanel.add(btnMaterials);
        menuPanel.add(btnIssueStock);
        menuPanel.add(btnReports);
        menuPanel.add(btnLogout);

        add(headerPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);

        pack();
        setSize(800, 520);
        setLocationRelativeTo(null);
    }

    private void registerEvents() {
        btnSuppliers.addActionListener(event -> {
            new SuppliersFrame().setVisible(true);
        });

        btnCleaners.addActionListener(event -> {
            new CleanersFrame().setVisible(true);
        });

        btnMaterials.addActionListener(event -> {
            new MaterialsFrame().setVisible(true);
        });

        btnIssueStock.addActionListener(event -> {
            new IssueStockFrame().setVisible(true);
        });

        btnReports.addActionListener(event -> {
            new ReportsFrame().setVisible(true);
        });

        btnLogout.addActionListener(event -> logout());
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DashboardFrame().setVisible(true);
        });
    }
}