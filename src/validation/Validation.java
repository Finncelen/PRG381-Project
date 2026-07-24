package validation;

import java.util.regex.Pattern;

public final class Validation {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(\\+27|0)[6-8][0-9]{8}$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z0-9_]{3,20}$");

    private Validation() {
        // Prevent objects from being created from this utility class.
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email)
                && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return false;
        }

        String cleanedNumber = phoneNumber
                .replace(" ", "")
                .replace("-", "");

        return PHONE_PATTERN.matcher(cleanedNumber).matches();
    }

    public static boolean isValidUsername(String username) {
        return !isEmpty(username)
                && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean passwordsMatch(
            String password,
            String confirmPassword
    ) {
        return password != null && password.equals(confirmPassword);
    }

    public static boolean isValidName(String name) {
        if (isEmpty(name)) {
            return false;
        }

        return name.trim().matches("[A-Za-zÀ-ÿ' -]{2,50}");
    }

    public static boolean isNonNegativeInteger(String value) {
        try {
            return Integer.parseInt(value.trim()) >= 0;
        } catch (NumberFormatException | NullPointerException exception) {
            return false;
        }
    }

    public static boolean isPositiveInteger(String value) {
        try {
            return Integer.parseInt(value.trim()) > 0;
        } catch (NumberFormatException | NullPointerException exception) {
            return false;
        }
    }

    public static boolean isNonNegativeDouble(String value) {
        try {
            return Double.parseDouble(value.trim()) >= 0;
        } catch (NumberFormatException | NullPointerException exception) {
            return false;
        }
    }

    public static boolean isPositiveDouble(String value) {
        try {
            return Double.parseDouble(value.trim()) > 0;
        } catch (NumberFormatException | NullPointerException exception) {
            return false;
        }
    }

    public static boolean hasEnoughStock(
            int quantityAvailable,
            int quantityRequested
    ) {
        return quantityRequested > 0
                && quantityRequested <= quantityAvailable;
    }

    public static String cleanText(String value) {
        return value == null ? "" : value.trim();
    }
}