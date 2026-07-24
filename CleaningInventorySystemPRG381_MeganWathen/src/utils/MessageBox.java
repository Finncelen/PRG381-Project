package utils;

import javax.swing.JOptionPane;
import java.awt.Component;

public class MessageBox {

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void warning(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static boolean confirm(Component parent, String message) {

        int option = JOptionPane.showConfirmDialog(
                parent,
                message,
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        return option == JOptionPane.YES_OPTION;
    }

}