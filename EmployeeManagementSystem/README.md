# 🧾 Employee Management System (Java + JDBC + MySQL)

A console-based **Employee Management System** built using **Core Java** and **JDBC** with **MySQL Database**.  
This project demonstrates how to connect Java applications with relational databases and perform advanced operations such as **CRUD, transactions, batch updates, and BLOB handling**.

---

## 🚀 Features

✅ Add new employees with salary, balance, and optional photo (BLOB)  
✅ View all employee records with a “Photo: Yes/No” status  
✅ Update employee salary  
✅ Delete employee by name  
✅ Transfer money between employees (Transaction handling with commit/rollback)  
✅ Insert multiple employees at once using Batch Processing  
✅ View Database and ResultSet Metadata dynamically  
✅ Store and retrieve employee photos (BLOB support)

---

## 🧰 Tech Stack

| Component | Technology |
|------------|-------------|
| Language | Java (JDK 8 or higher) |
| Database | MySQL |
| Connector | MySQL Connector/J (JDBC Driver) |
| IDE (Recommended) | IntelliJ IDEA / Eclipse / VS Code |
| Build Tool | Manual or Maven (optional) |

---

## 🗂️ Project Structure

EmployeeManagementSystem/
│

├── src/

│ ├── EmployeeCrud.java # Handles all database logic (CRUD, Transaction, Batch, BLOB, Metadata)

│ └── Main.java # Menu-driven console UI for the system

│

├── lib/

│ └── mysql-connector-j-8.0.xx.jar # MySQL JDBC Driver

│

├── db_script.sql # SQL script to create database & table

├── README.md # Project documentation

└── .gitignore



## ⚙️ Database Setup

1. Open MySQL Workbench or Terminal.  
2. Run the following script (or execute `db_script.sql`):


CREATE DATABASE IF NOT EXISTS testdb;
USE testdb;

CREATE TABLE employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    salary DOUBLE,
    balance DOUBLE,
    photo LONGBLOB
);

INSERT INTO employee (name, salary, balance) VALUES
('Raja', 55000, 2000),
('Shereep', 60000, 2500);
🧮 Run the Application
🔹 Option 1: Using IntelliJ IDEA
Clone this repository

bash
Copy code
git clone https://github.com/Raja-shereep/Java-Development.git
Open in IntelliJ

Add the MySQL Connector .jar from lib/ to the classpath

Run Main.java

🔹 Option 2: Using Command Line
bash
Copy code
javac -cp "lib/mysql-connector-j-8.0.xx.jar" src/*.java -d bin
java -cp "bin;lib/mysql-connector-j-8.0.xx.jar" Main
💡 Key Concepts Demonstrated
Concept	Description

JDBC Connection	    - Connect Java to MySQL

PreparedStatement	- Prevent SQL injection

Transactions	    - Commit/Rollback mechanism

Batch Processing	- Efficient multi-inserts

Metadata	        - Extract database and result info

BLOB Handling	    - Store and retrieve images




🧾 About the Author
👨‍💻 Raja Shereep M
Java Developer | B.Tech (CSE) | GATE Aspirant

🔗 https://www.linkedin.com/in/raja-shereep-m/
📧 rshereep23@gmail.com
