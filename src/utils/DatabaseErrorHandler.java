package utils;

import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public final class DatabaseErrorHandler {

    private DatabaseErrorHandler() {
    }

    public static void showError(
            Component parent,
            String action,
            SQLException exception
    ) {

        System.err.println(
                action + ": " + exception.getMessage()
        );

        JOptionPane.showMessageDialog(
                parent,
                "The system could not complete the operation.\n"
                + "Please try again.\n\n"
                + "Details: "
                + getFriendlyMessage(exception),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private static String getFriendlyMessage(
            SQLException exception
    ) {

        String message = exception.getMessage();

        if (message == null) {
            return "Unknown database error.";
        }

        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("unique")) {
            return "A record with the same information already exists.";
        }

        if (lowerMessage.contains("foreign key")) {
            return "This record is being used elsewhere and cannot be removed.";
        }

        if (lowerMessage.contains("not null")) {
            return "A required value was not provided.";
        }

        if (lowerMessage.contains("check constraint")) {
            return "One of the entered values is invalid.";
        }

        return message;
    }
}
