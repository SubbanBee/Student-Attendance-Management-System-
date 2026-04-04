-- ============================================
-- FULL SETUP - Run as root (one-time setup)
-- Command: mysql -u root -p < create_fresh_database.sql
-- ============================================

-- 1. Create MySQL user ams_user (password: password123)
DROP USER IF EXISTS 'ams_user'@'localhost';
CREATE USER 'ams_user'@'localhost' IDENTIFIED BY 'password123';

-- 2. Create database
CREATE DATABASE IF NOT EXISTS college_erp;
USE college_erp;

-- 3. Grant all permissions to ams_user
GRANT ALL PRIVILEGES ON college_erp.* TO 'ams_user'@'localhost';
FLUSH PRIVILEGES;

-- 4. Create tables
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  department VARCHAR(50),
  user_id INT NULL
);

CREATE TABLE IF NOT EXISTS subjects (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS student_subject (
  id INT AUTO_INCREMENT PRIMARY KEY,
  student_id INT NOT NULL,
  subject_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS attendance (
  id INT AUTO_INCREMENT PRIMARY KEY,
  student_id INT NOT NULL,
  subject_id INT NOT NULL,
  date DATE NOT NULL,
  status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS faculty (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  username VARCHAR(50) NOT NULL
);

-- 5. Insert default login users
INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');
INSERT IGNORE INTO users (username, password, role) VALUES ('faculty1', 'faculty123', 'faculty');
INSERT IGNORE INTO users (username, password, role) VALUES ('student1', 'student123', 'student');

-- 6. Sample data
INSERT IGNORE INTO students (name, department) VALUES ('John Doe', 'CSE');
INSERT IGNORE INTO students (name, department) VALUES ('Jane Smith', 'ECE');
INSERT IGNORE INTO subjects (name) VALUES ('Math');
INSERT IGNORE INTO subjects (name) VALUES ('Physics');

SELECT 'Done! Database + ams_user created. Login: admin/admin123' AS result;
