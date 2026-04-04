-- Run as ams_user (no root needed)
-- Command: mysql -u ams_user -p college_erp < setup_as_ams_user.sql
-- Database college_erp must already exist

USE college_erp;

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL
);

INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');
INSERT IGNORE INTO users (username, password, role) VALUES ('faculty1', 'faculty123', 'faculty');
INSERT IGNORE INTO users (username, password, role) VALUES ('student1', 'student123', 'student');

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

INSERT IGNORE INTO students (name, department) VALUES ('John Doe', 'CSE');
INSERT IGNORE INTO students (name, department) VALUES ('Jane Smith', 'ECE');
INSERT IGNORE INTO subjects (name) VALUES ('Math');
INSERT IGNORE INTO subjects (name) VALUES ('Physics');

SELECT 'Setup complete! Login with admin/admin123' AS message;
