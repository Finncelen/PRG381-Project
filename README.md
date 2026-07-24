# Cleaning Inventory System

A Java Swing desktop application developed for managing cleaning inventory within a university environment.

The system allows administrators to manage suppliers, cleaners, cleaning materials, stock issuance and reports through an easy-to-use graphical interface.

---

## Features

- User Login and Registration
- Supplier Management
- Cleaner Management
- Material Management
- Stock Issuance
- Inventory Tracking
- Low Stock Monitoring
- Reporting
- Input Validation
- Password Hashing
- SQLite Database Integration

---

## Technologies Used

- Java
- Java Swing
- SQLite
- JDBC
- Apache NetBeans
- Git & GitHub

---

## Database

This project uses **SQLite** as its database.

SQLite was chosen because it is lightweight, serverless and stores all data in a single database file, making the application easy to run without installing or configuring a database server.

The database contains the following tables:

- Users
- Suppliers
- Cleaners
- Materials
- Stock Issuances

---

## Project Structure

```
src/
│
├── dao/
├── database/
├── exceptions/
├── gui/
├── main/
├── models/
├── reports/
├── utils/
└── validation/
```

---

## Main Screens

- Login
- Register
- Dashboard
- Suppliers
- Cleaners
- Materials
- Issue Stock
- Reports

---

## Inventory Features

The system allows users to:

- Add new suppliers
- Register cleaners
- Add and update cleaning materials
- Issue stock to cleaners
- Monitor available stock levels
- Generate reports
- Track low stock items using reorder levels

---

## Security

Passwords are stored using hashing before being saved to the database.

Input validation is implemented throughout the application to help ensure data integrity.

---

## How to Run

### Requirements

- Java JDK 17 or later
- Apache NetBeans
- SQLite JDBC Driver

### Steps

1. Clone the repository

```
git clone 
```

2. Open the project in Apache NetBeans.

3. Add the SQLite JDBC driver to the project libraries.

4. Place the `cleaning_inventory.db` file inside the project's `database` folder.

5. Build and run the project.

---

## Future Improvements

- Password recovery
- Advanced search and filtering
- Export reports to PDF
- User activity logs
- Email notifications for low stock
- Dashboard statistics

---

## Author

**Megan Heather Wathen**

Belgium Campus iTversity

PRG381 Project

2026

---

## License

This project was developed for educational purposes as part of the PRG381 module at Belgium Campus iTversity.
