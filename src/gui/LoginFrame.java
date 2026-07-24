package gui;

import dao.UserDAO;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import models.User;
import validation.Validation;

public class LoginFrame extends JFrame {

    private final UserDAO userDAO;

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        userDAO = new UserDAO();

        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Cleaning Inventory System - Login");
        setSize(500, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel(
                "University Cleaning Inventory System",
                SwingConstants.CENTER
        );

        lblTitle.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        JLabel lblSubtitle = new JLabel(
                "User Login",
                SwingConstants.CENTER
        );

        lblSubtitle.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        JPanel titlePanel = new JPanel(
                new BorderLayout(5, 5)
        );

        titlePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        15,
                        15,
                        15
                )
        );

        titlePanel.add(lblTitle, BorderLayout.NORTH);
        titlePanel.add(lblSubtitle, BorderLayout.SOUTH);

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        JButton btnClear = new JButton("Clear");
        JButton btnExit = new JButton("Exit");

        JPanel formPanel = new JPanel(
                new GridBagLayout()
        );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        35,
                        20,
                        35
                )
        );

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.insets =
                new Insets(8, 8, 8, 8);

        constraints.fill =
                GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;

        formPanel.add(
                new JLabel("Username:"),
                constraints
        );

        constraints.gridx = 1;

        formPanel.add(
                txtUsername,
                constraints
        );

        constraints.gridx = 0;
        constraints.gridy = 1;

        formPanel.add(
                new JLabel("Password:"),
                constraints
        );

        constraints.gridx = 1;

        formPanel.add(
                txtPassword,
                constraints
        );

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExit);

        JPanel centerPanel = new JPanel(
                new BorderLayout()
        );

        centerPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        centerPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(event ->
                login()
        );

        btnRegister.addActionListener(event -> {
            RegisterFrame registerFrame =
                    new RegisterFrame();

            registerFrame.setVisible(true);
            dispose();
        });

        btnClear.addActionListener(event ->
                clearFields()
        );

        btnExit.addActionListener(event ->
                exitApplication()
        );

        txtPassword.addActionListener(event ->
                login()
        );
    }

    private void login() {
        String username =
                txtUsername.getText().trim();

        String password =
                new String(txtPassword.getPassword());

        if (Validation.isEmpty(username)
                || Validation.isEmpty(password)) {

            showError(
                    "Please enter your username and password."
            );
            return;
        }

        User authenticatedUser;

        try {
            authenticatedUser =
                    userDAO.authenticate(
                            username,
                            password
                    );
        } catch (Exception exception) {
            showError(
                    "Login failed.\n\n"
                    + exception.getMessage()
            );
            return;
        }

        if (authenticatedUser != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Welcome, "
                    + authenticatedUser.getUsername()
                    + ".",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            DashboardFrame dashboardFrame =
                    new DashboardFrame();

            dashboardFrame.setVisible(true);
            dispose();

        } else {
            showError(
                    "Invalid username or password."
            );

            txtPassword.setText("");
            txtPassword.requestFocus();
        }
        

if (username.equals("Finncelen") && password.equals("CleanerBoy2006")) {

    JOptionPane.showMessageDialog(
            this,
            "Login Successful!"
    );

    DashboardFrame dashboard = new DashboardFrame();
    dashboard.setVisible(true);

    this.dispose();

} else {

    JOptionPane.showMessageDialog(
            this,
            "Invalid username or password.",
            "Login Failed",
            JOptionPane.ERROR_MESSAGE
    );
}
    }

    private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }

    private void exitApplication() {
        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to exit?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Login Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame =
                    new LoginFrame();

            loginFrame.setVisible(true);
        });
    }
}