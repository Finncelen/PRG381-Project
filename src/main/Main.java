
package main;
import javax.swing.SwingUtilities;
import gui.LoginFrame;

/**
 *
 * @author Finncelen
 */


public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
