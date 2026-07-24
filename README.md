# University Cleaning Inventory & Issuance System

## Overview

The University Cleaning Inventory & Issuance System is a Java Swing desktop application developed for the PRG381 Project at Belgium Campus iTversity.

The system provides an efficient way for university staff to manage cleaning materials, suppliers, cleaners, and stock issuance while maintaining inventory records through a user-friendly desktop interface.

The application follows Object-Oriented Programming principles and uses JDBC to connect to a PostgreSQL database.

---

## Features

### User Authentication

- Secure user login
- Staff registration
- Password validation
- User roles (Storekeeper and Supervisor)
- Logout functionality

---

### Dashboard

Displays an overview of the system including:

- Total Materials
- Total Suppliers
- Total Cleaners
- Recent Stock Issuances
- Low Stock Alerts

---

### Materials Management

Users can:

- Add materials
- View materials
- Update material information
- Delete materials
- Search materials
- Monitor stock quantities
- Set reorder levels

---

### Suppliers Management

Users can:

- Add suppliers
- Edit supplier information
- Delete suppliers
- Search suppliers
- Maintain supplier contact details

---

### Cleaners Management

Users can:

- Register cleaners
- Edit cleaner information
- Remove cleaners
- Search cleaners
- Assign cleaners to departments

---

### Stock Issuance

The system allows staff to:

- Issue cleaning materials
- Automatically deduct stock quantities
- Prevent issuing more stock than available
- Store issuance history

---

### Reports

Generate reports for:

- Inventory
- Low Stock Materials
- Stock Issuance History
- Material Usage

---

### Validation

The system validates:

- Required fields
- Duplicate usernames
- Duplicate email addresses
- Negative stock quantities
- Available inventory before issuing stock
- Valid email format
- Password requirements

Meaningful error messages are displayed whenever validation fails.

---

## Technologies Used

- Java 24
- Java Swing
- JDBC
- PostgreSQL
- NetBeans IDE
- Git & GitHub

---

## Object-Oriented Programming Principles

This project demonstrates:

### Encapsulation

All model classes use private fields with public getters and setters.

### Inheritance

Shared behaviour is implemented using inheritance where appropriate.

### Abstraction

DAO classes separate database logic from business logic.

### Polymorphism

Methods are designed to support reusable and extendable functionality.

---

## Project Structure

```
src
│
├── dao
│   ├── CleanerDAO
│   ├── MaterialDAO
│   ├── StockIssuanceDAO
│   ├── SupplierDAO
│   └── UserDAO
│
├── database
│
├── gui
│   ├── LoginFrame
│   ├── RegisterFrame
│   ├── DashboardFrame
│   ├── MaterialsFrame
│   ├── SuppliersFrame
│   ├── CleanersFrame
│   ├── StockIssuanceFrame
│   └── ReportsFrame
│
├── models
│
├── reports
│
├── utils
│
└── validation
```

---

## Database

The application uses PostgreSQL.

Main tables include:

- users
- materials
- suppliers
- cleaners
- stock_issuances

The database is accessed using JDBC.

---

## Installation

### Requirements

- Java JDK 24
- PostgreSQL
- NetBeans IDE

### Steps

1. Clone the repository

```
git clone https://github.com/Finncelen/PRG381-Project.git
```

2. Create the PostgreSQL database.

3. Execute the SQL script included in the project.

4. Update the database connection settings.

5. Open the project in NetBeans.

6. Build and run the application.

---

## Business Rules

- Users cannot register duplicate usernames.
- Users cannot register duplicate email addresses.
- Materials cannot have negative stock quantities.
- Materials cannot be issued if insufficient stock exists.
- All required fields must be completed.
- Stock levels update automatically after each issuance.

---

## Future Improvements

Potential future enhancements include:

- Barcode scanning
- Email notifications
- Export reports to PDF
- QR code inventory tracking
- Audit logs
- Advanced reporting dashboards
- Role-based permissions with granular access control

---

## Team Members

- Megan Wathen
- *(Add remaining group members here)*

---

## Module

PRG381 – Programming 381

Belgium Campus iTversity

2026

---

## License

This project was developed for educational purposes as part of the PRG381 module at Belgium Campus iTversity.
