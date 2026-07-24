package gui;

import dao.UserDAO;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import models.User;
import utils.PasswordHasher;
import validation.Validation;

public class RegisterFrame extends JFrame {

    private final UserDAO userDAO;

    private JTextField txtFullName;
    private JTextField txtUsername;
    private JTextField txtEmail;

    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    private JComboBox<String> cmbRole;

    public RegisterFrame() {
        userDAO = new UserDAO();

        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle(
                "Cleaning Inventory System - Register"
        );

        setSize(600, 540);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel(
                "Create User Account",
                SwingConstants.CENTER
        );

        lblTitle.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        lblTitle.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        10,
                        15,
                        10
                )
        );

        txtFullName = new JTextField(22);
        txtUsername = new JTextField(22);
        txtEmail = new JTextField(22);

        txtPassword = new JPasswordField(22);
        txtConfirmPassword =
                new JPasswordField(22);

        cmbRole = new JComboBox<>(
                new String[]{
                    "Staff",
                    "Administrator"
                }
        );

        JPanel formPanel = new JPanel(
                new GridBagLayout()
        );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        45,
                        15,
                        45
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
                new JLabel("Full Name:"),
                constraints
        );

        constraints.gridx = 1;

        formPanel.add(
                txtFullName,
                constraints
        );

        constraints.gridx = 0;
        constraints.gridy = 1;

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
        constraints.gridy = 2;

        formPanel.add(
                new JLabel("Email Address:"),
                constraints
        );

        constraints.gridx = 1;

        formPanel.add(
                txtEmail,
                constraints
        );

        constraints.gridx = 0;
        constraints.gridy = 3;

        formPanel.add(
                new JLabel("Password:"),
                constraints
        );

        constraints.gridx = 1;

        formPanel.add(
                txtPassword,
                constraints
        );

        constraints.gridx = 0;
        constraints.gridy = 4;

        formPanel.add(
                new JLabel("Confirm Password:"),
                constraints
        );

        constraints.gridx = 1;

        formPanel.add(
                txtConfirmPassword,
                constraints
        );

        constraints.gridx = 0;
        constraints.gridy = 5;

        formPanel.add(
                new JLabel("Role:"),
                constraints
        );

        constraints.gridx = 1;

        formPanel.add(
                cmbRole,
                constraints
        );

        JButton btnRegister =
                new JButton("Register");

        JButton btnClear =
                new JButton("Clear");

        JButton btnBack =
                new JButton("Back to Login");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);

        add(lblTitle, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnRegister.addActionListener(event ->
                registerUser()
        );

        btnClear.addActionListener(event ->
                clearFields()
        );

        btnBack.addActionListener(event ->
                returnToLogin()
        );

        txtConfirmPassword.addActionListener(
                event -> registerUser()
        );
    }

    private void registerUser() {
        String fullName =
                txtFullName.getText().trim();

        String username =
                txtUsername.getText().trim();

        String email =
                txtEmail.getText().trim();

        String password =
                new String(txtPassword.getPassword());

        String confirmPassword =
                new String(
                        txtConfirmPassword.getPassword()
                );

        String role =
                cmbRole.getSelectedItem().toString();

        if (Validation.isEmpty(fullName)
                || Validation.isEmpty(username)
                || Validation.isEmpty(email)
                || Validation.isEmpty(password)
                || Validation.isEmpty(
                        confirmPassword
                )) {

            showError(
                    "Please complete all registration fields."
            );
            return;
        }

        if (!Validation.isValidName(fullName)) {
            showError(
                    "Enter a valid full name using letters, "
                    + "spaces, apostrophes or hyphens."
            );
            return;
        }

        if (!Validation.isValidUsername(username)) {
            showError(
                    "The username must contain between "
                    + "4 and 20 letters, numbers or underscores."
            );
            return;
        }

        if (!Validation.isValidEmail(email)) {
            showError(
                    "Enter a valid email address."
            );
            return;
        }

        if (!Validation.isValidPassword(password)) {
            showError(
                    "The password must contain at least "
                    + "8 characters, including an uppercase "
                    + "letter, lowercase letter and number."
            );
            return;
        }

        if (!Validation.passwordsMatch(
                password,
                confirmPassword
        )) {
            showError(
                    "The passwords do not match."
            );
            return;
        }

        if (userDAO.usernameExists(username)) {
            showError(
                    "That username is already registered."
            );
            return;
        }

        if (userDAO.emailExists(email)) {
            showError(
                    "That email address is already registered."
            );
            return;
        }

        String hashedPassword;

        try {
            hashedPassword =
                    PasswordHasher.hashPassword(
                            password
                    );
        } catch (Exception exception) {
            showError(
                    "The password could not be secured.\n\n"
                    + exception.getMessage()
            );
            return;
        }

        User user = new User();

        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole(role);
        user.setActive(true);

        boolean successful;

        try {
            successful =
                    userDAO.registerUser(user);
        } catch (Exception exception) {
            showError(
                    "Registration failed.\n\n"
                    + exception.getMessage()
            );
            return;
        }

        if (successful) {
            JOptionPane.showMessageDialog(this, """
                                                Registration completed successfully.
                                                You may now log in.""",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            returnToLogin();

        } else {
            showError(
                    "The account could not be registered."
            );
        }
    }

    private void clearFields() {
        txtFullName.setText("");
        txtUsername.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");

        cmbRole.setSelectedIndex(0);
        txtFullName.requestFocus();
    }

    private void returnToLogin() {
        LoginFrame loginFrame =
                new LoginFrame();

        loginFrame.setVisible(true);
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Registration Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegisterFrame registerFrame =
                    new RegisterFrame();

            registerFrame.setVisible(true);
        });
    }
}