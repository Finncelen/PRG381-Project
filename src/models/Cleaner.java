
package models;

/**
 *
 * @author Finncelen
 */

public class Cleaner {

    private int cleanerId;
    private String firstName;
    private String lastName;
    private String employeeNumber;
    private String phoneNumber;
    private String email;
    private String department;
    private boolean active;

    public Cleaner() {
        this.active = true;
    }

    public Cleaner(
            int cleanerId,
            String firstName,
            String lastName,
            String employeeNumber,
            String phoneNumber,
            String email,
            String department,
            boolean active
    ) {
        this.cleanerId = cleanerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.department = department;
        this.active = active;
    }

    public int getCleanerId() {
        return cleanerId;
    }

    public void setCleanerId(int cleanerId) {
        this.cleanerId = cleanerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return employeeNumber + " - " + getFullName();
    }
}