# College ERP — Student Attendance Management System

Role-based Java desktop application (Admin, Faculty, Student) for managing attendance and basic academic records using a MySQL backend.

---

## Overview

This is a desktop ERP-style application built with Java (Swing) and MySQL. It demonstrates a simple, complete flow: GUI → JDBC → MySQL database. The project is suitable as a learning project for database-backed Java desktop applications.

## Key features

- Role-based login: Admin, Faculty, Student
- Admin dashboard: manage students, subjects, users; view attendance; export reports
- Faculty dashboard: mark attendance, view attendance, charts
- Student dashboard: view personal attendance
- Export attendance reports to PDF

## Tech stack

- Java (Swing/AWT) — desktop GUI
- MySQL — relational database
- JDBC (mysql-connector)
- Batch scripts for running on Windows (`run_attendance.bat` / `run_erp.bat`)

## Prerequisites

- Java JDK 8 or newer
- MySQL server
- MySQL Connector/J (JAR) — do NOT keep connector JAR in repo history for production. Download it from https://dev.mysql.com/downloads/connector/j/ if not present.

## Setup & Installation

1. Clone the repository:

```bash
git clone https://github.com/SubbanBee/Student-Attendance-Management-System-.git
cd Student-Attendance-Management-System-
```

2. Create and populate the database

Run the SQL scripts shipped in the repo to create database and default tables:

```bash
mysql -u root -p < database_setup.sql
# or
mysql -u root -p < create_fresh_database.sql
```

If you want to create a dedicated user for the app, run:

```bash
mysql -u root -p < setup_ams_user.sql
```

3. Update DB credentials

Open `DBConnection.java` and set your `DB_USER` and `DB_PASS` values to match your MySQL user credentials.

4. Add MySQL Connector to classpath

Place the MySQL connector JAR on your classpath or update `run_attendance.bat` and `run_erp.bat` to point to the connector. Example using the included batch (Windows):

- Open `run_attendance.bat` and set the `MYSQL_CONNECTOR` path to where you placed `mysql-connector-j-<version>.jar`.

5. Compile and run (example via command line)

```bash
javac *.java
java AcademicSystem
```

Windows users can use the provided `.bat` files after updating paths.

## Default login accounts

| Username | Password   | Role    |
|----------|------------|---------|
| admin    | admin123   | Admin   |
| faculty1 | faculty123 | Faculty |
| student1 | student123 | Student |

## Project structure (top-level)

```
Student-Attendance-Management-System-
├── AcademicSystem.java
├── AdminDashboard.java
├── FacultyDashboard.java
├── StudentDashboard.java
├── DBConnection.java
├── database_setup.sql
├── create_fresh_database.sql
├── setup_ams_user.sql
├── run_attendance.bat
├── run_erp.bat
├── README.md
├── lib/ (optional libraries)
└── mysql-connector-j-<version>.jar (remove from repo; use Releases or download URL)
```

## Notes & recommendations

- The repository currently contains `mysql-connector-j-9.6.0.jar`. It's best practice to remove large binary dependencies from the repository and add them to Releases or instruct users to download. If you want, I can provide exact git commands to remove it from history (requires your confirmation).
- Consider migrating the project to a build system such as Maven or Gradle to manage dependencies and packaging.
- Organize Java sources into `src/main/java` packages and resources into `src/main/resources` for a cleaner layout.

## Screenshots / Demo

Add screenshots here to show: login screen, admin dashboard, faculty attendance marking screen, student dashboard.

![Screenshot: Login](/path/to/screenshot-login.png)

(Replace placeholders with actual images committed to the repo in a `docs/` or `assets/` folder.)

## Future improvements

- Migrate to Maven/Gradle and remove binary JAR from the repo
- Add unit tests around DB access code
- Add an installer or packaged executable (EXE/JAR) and cross-platform run scripts
- Refactor into packages and apply MVC separation

## License

Add a LICENSE if you want to explicitly state reuse terms. If you want, I can add an MIT or Apache-2.0 license file.

## Contact

If you want this project polished further (packaging, screenshots, or migrating JAR out of repo), tell me and I will prepare the changes and open PRs where needed.
