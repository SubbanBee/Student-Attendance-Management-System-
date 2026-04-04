# College ERP - Attendance System

Role-based login system with Admin, Faculty, and Student dashboards.

---

## Requirements

- Java 8+ (JDK)
- MySQL
- MySQL Connector JAR (mysql-connector-j-*.jar)

---

## Setup (run once)

### 1. Create database and tables

Run as **root** in MySQL:

```cmd
mysql -u root -p < database_setup.sql
```

Or in MySQL Workbench: open `database_setup.sql` and execute.

### 2. Grant ams_user permissions (if not already done)

```cmd
mysql -u root -p < setup_ams_user.sql
```

### 3. Update DBConnection.java

In `DBConnection.java`, set `DB_PASS` to your `ams_user` password (default: `password123`).

### 4. Update run_attendance.bat

Set `MYSQL_CONNECTOR` to the path of your `mysql-connector-j-*.jar` file.

---

## Run the application

**Option A – Double-click:** `run_attendance.bat`

**Option B – Command line:**
```cmd
cd c:\Users\subba\OneDrive\Desktop\AttendanceProject
set CLASSPATH=.;C:\Users\subba\Downloads\mysql-connector-j-9.6.0.jar
javac *.java
java AcademicSystem
```

---

## Default login accounts

| Username | Password | Role    |
|----------|----------|---------|
| admin    | admin123 | Admin   |
| faculty1 | faculty123 | Faculty |
| student1 | student123 | Student |

---

## Features

- **Admin:** Add/View/Delete students, Add subjects, Assign subjects, Register users, View attendance, Charts, Export
- **Faculty:** Mark attendance, View attendance, Attendance chart
- **Student:** View my attendance
- **Register:** Create new users (from login screen)
- **Logout:** Available on all dashboards
