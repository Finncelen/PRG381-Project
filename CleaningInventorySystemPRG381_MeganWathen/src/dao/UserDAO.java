
package dao;
import java.util.ArrayList;
import java.util.List;
import models.User;

/**
 *
 * @author Finncelen
 */



public class UserDAO {

    private static final List<User> users = new ArrayList<>();
    private static int nextId = 1;

    static {
        users.add(new User(
                nextId++,
                "System Supervisor",
                "admin",
                "admin@campus.ac.za",
                "admin123",
                "SUPERVISOR",
                true
        ));
    }

    public boolean registerUser(User user) {
        if (usernameExists(user.getUsername())) {
            return false;
        }

        if (emailExists(user.getEmail())) {
            return false;
        }

        user.setUserId(nextId++);
        users.add(user);
        return true;
    }

    public User authenticate(String username, String password) {
        for (User user : users) {
            boolean usernameMatches =
                    user.getUsername().equalsIgnoreCase(username);

            boolean passwordMatches =
                    user.getPassword().equals(password);

            if (usernameMatches
                    && passwordMatches
                    && user.isActive()) {
                return user;
            }
        }

        return null;
    }

    public boolean usernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    public boolean emailExists(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
