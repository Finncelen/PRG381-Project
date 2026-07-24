# University Cleaning Inventory & Issuance System

## Project Overview

The **University Cleaning Inventory & Issuance System** is a Java Desktop Application developed as part of the Programming 3781 project at Belgium Campus iTversity.

The purpose of the system is to assist university staff with managing cleaning inventory efficiently. The application allows authorized users to manage cleaning materials, suppliers, cleaners, stock issuances, and inventory records through an easy-to-use desktop interface.

The project follows **Track B**, which requires the use of Java Swing, Core Java, Object-Oriented Programming (OOP), JDBC, and a relational database.

---

# Features

## User Authentication

* User Login
* User Registration
* Password Validation
* User Role Management
* Secure Logout

## Dashboard

* View total cleaning materials
* View total cleaners
* View low stock items
* View recent stock issuances
* Quick navigation to system modules

## Materials Management

* Add materials
* View materials
* Update material information
* Delete materials
* Search materials
* Track available quantities
* Monitor reorder levels

## Suppliers Management

* Add suppliers
* View suppliers
* Update supplier details
* Delete suppliers
* Store supplier contact information

## Cleaners Management

* Add cleaners
* View cleaners
* Update cleaner information
* Delete cleaners
* Assign cleaners to departments (optional)

## Stock Issuance

* Issue cleaning materials to cleaners
* Automatically deduct stock quantities
* Prevent issuing more stock than available
* Maintain issuance history

## Reports

* Inventory Report
* Low Stock Report
* Issuance History Report
* Material Usage Report

---

# Technologies Used

* Java
* Java Swing
* Core Java
* JDBC
* PostgreSQL (or Derby/JavaDB)
* NetBeans IDE
* GitHub

---

# Object-Oriented Programming Principles

The project demonstrates the four core OOP principles:

### Encapsulation

Private class variables with public getters and setters.

### Inheritance

Classes such as `Cleaner` inherit from the `Person` class.

### Abstraction

Abstract classes and methods are used where appropriate.

### Polymorphism

Overridden methods demonstrate different behaviors across subclasses.

---

# Project Structure

```text
UniversityCleaningInventory

│
├── src
│
├── database
│      DBConnection.java
│
├── model
│      Person.java
│      User.java
│      Cleaner.java
│      Supplier.java
│      Material.java
│      StockIssuance.java
│
├── dao
│      UserDAO.java
│      MaterialDAO.java
│      SupplierDAO.java
│      CleanerDAO.java
│      StockDAO.java
│
├── gui
│      LoginForm.java
│      RegisterForm.java
│      Dashboard.java
│      MaterialForm.java
│      SupplierForm.java
│      CleanerForm.java
│      StockIssueForm.java
│      ReportsForm.java
│
├── validation
│      Validation.java
│
└── Main.java
```

---

# Database Tables

The system uses the following database tables:

* users
* materials
* suppliers
* cleaners
* stock_issuance

---

# Installation

1. Clone the repository.

```bash
[git clone <repository-url>](https://github.com/Mogau-art/PRG381-Project.git)
```

2. Open the project in NetBeans.

3. Create the project database.

4. Import the SQL database script.

5. Update the database connection inside `DBConnection.java`.

6. Add the PostgreSQL JDBC Driver.

7. Build and run the project.

---

# Business Rules

The system enforces the following business rules:

* Duplicate usernames are not allowed.
* Duplicate email addresses are not allowed.
* Required fields must be completed.
* Negative stock quantities are not allowed.
* Stock cannot be issued if insufficient inventory is available.
* Inventory is automatically updated after every stock issuance.
* Meaningful validation and error messages are displayed to users.

---

# Reports

The system generates the following reports:

* Inventory Report
* Low Stock Report
* Issuance History Report
* Material Usage Report

These reports provide staff with an overview of inventory levels and stock movement.

---

# Exception Handling

The application includes exception handling for:

* Database connection errors
* SQL exceptions
* Invalid user input
* Number format exceptions
* Null pointer exceptions

This improves system reliability and user experience.

---

# Future Improvements

Possible future enhancements include:

* Barcode scanner integration
* QR Code support
* Email notifications for low stock
* Export reports to PDF
* Export reports to Excel
* Automatic supplier ordering
* Audit logs
* User profile management
* Dark mode

---

# My  Responsibilities

| Responsibility                                           |
| -------------------------------------------------------- |
| Database, Authentication, Dashboard                      |
| Materials Management and Suppliers                       |
| Cleaners Management and Stock Issuance                   |
| Reports, Validation, OOP, Exception Handling and Testing |

---

# Testing

The system was tested to verify:

* User authentication
* CRUD functionality
* Database connectivity
* Inventory management
* Stock deductions
* Report generation
* Input validation
* Exception handling

---

# Author

Finncelen

Programming 3781 Project

Belgium Campus iTversity

University Cleaning Inventory & Issuance System

2026
