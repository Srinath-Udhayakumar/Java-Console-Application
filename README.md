# BlueVerse Bank Console Application

A robust console-based banking system developed in Core Java, demonstrating key concepts of Object-Oriented Programming (OOP), the DAO pattern, and data persistence using JDBC with a PostgreSQL database.

---

## 🚀 Technical Stack
| Component | Technology/Tool |
| :--- | :--- |
| **Language** | Java (Core) |
| **Database** | PostgreSQL |
| **Connectivity** | JDBC (Java Database Connectivity) |
| **Build Tool** | Manual Compilation (Standard Java Application) |

## ✨ Key Features
* **User Authentication:** Secure Sign In/Sign Up flow.
* **Account Operations:** Deposit, Withdrawal, Balance Check, and Password Update.
* **Business Logic Enforcement:** Validation for minimum initial deposit ($1000), minimum withdrawal ($100), and password length (4 chars).
* **Transaction Tracking:** Logs all deposits and withdrawals to the database, providing a full history to the user.
* **Admin Access:** Separate login for bank staff to view total accounts, total balance, and all user details.

## 🛠️ Architecture and Design Highlights

This project utilizes a **Layered Architecture** following the **Data Access Object (DAO) Pattern** for maximum modularity and separation of concerns:

1.  **Model (`BankAccount.java`):** Encapsulates data (user, balance, password) and core account business logic (deposit, withdraw, changePassword).
2.  **DAO (`TransactionDAO.java`):** Abstracts all database interaction. Focuses on safe, secure data operations using **`PreparedStatement`** to prevent SQL Injection.
3.  **Controller (`BankingApp.java`):** Handles user input/output and orchestrates the application flow by interacting with the Model and DAO layers.

**Security and Integrity:**
* Uses Java **`try-with-resources`** for guaranteed closing of database connections and statements, preventing resource leaks.
* Database schema enforces **Referential Integrity** using Foreign Keys.

## ⚙️ Setup and Installation

Follow these steps to get the application running on your local machine.

### 1. Prerequisites

You must have the following installed:
* Java Development Kit (JDK 11 or higher)
* PostgreSQL Database
* PostgreSQL JDBC Driver (This needs to be included in your project's classpath when compiling/running.)

### 2. Database Configuration

1.  Create the database:
    ```bash
    CREATE DATABASE bank_db; 
    ```
2.  Update the connection details in `src/com/consoleapps/bankingapp/db/DBConnectionUtil.java` if you use different credentials:
    ```java
    public static final String URL = "jdbc:postgresql://localhost:5432/bank_db";
    public static final String USER = "[YOUR POSTGRES USERNAME]"; // e.g., postgres
    public static final String PASS = "[YOUR POSTGRES PASSWORD]"; // e.g., 123456
    ```
3.  Execute the setup script to create the tables:
    ```bash
    psql -d bank_db -f sql/setup.sql
    ```

### 3. Running the Application

1.  Compile the Java files, including the necessary PostgreSQL JDBC driver JAR in the classpath:
    ```bash
    # Assuming the driver is named postgresql-42.2.8.jar and is in your project root
    javac -cp .:postgresql-42.2.8.jar src/com/consoleapps/bankingapp/controller/BankingApp.java 
    ```
2.  Run the application:
    ```bash
    java -cp .:postgresql-42.2.8.jar com.consoleapps.bankingapp.controller.BankingApp
    ```

## 👨‍💻 Author

**Srinath Udhayakumar**
* **Final Year UG ECE Student** at Vel Tech Multi Tech, Chennai
* **[www.linkedin.com/in/srinath-udhayakumar-14sep2000]**


---