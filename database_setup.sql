-- Drop the database if it exists
DROP DATABASE IF EXISTS college_erp;

-- Create the complete schema
CREATE DATABASE college_erp;
USE college_erp;

-- 1. Users table (for authentication)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'faculty', 'student', 'parent') NOT NULL
);

-- 2. Courses table
CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    credits INT DEFAULT 3
);

-- 3. Students table
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    user_id INT,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 4. Parents table
CREATE TABLE parents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    student_id INT NOT NULL,
    user_id INT,
    FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. Faculty table
CREATE TABLE faculty (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    user_id INT,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 6. Student-Course Enrollments 
CREATE TABLE student_courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE KEY (student_id, course_id)
);

-- 7. Timetable (Course Scheduling)
CREATE TABLE timetables (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    faculty_id INT NOT NULL,
    day_of_week VARCHAR(15) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY(faculty_id) REFERENCES faculty(id) ON DELETE CASCADE
);

-- 8. Attendance
CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    status ENUM('Present', 'Absent', 'Late') NOT NULL,
    marked_by INT,
    FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY(marked_by) REFERENCES faculty(id) ON DELETE SET NULL,
    UNIQUE KEY (date, student_id, course_id)
);

-- 9. Notifications (Admin -> Parent for < 75%)
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    parent_id INT NOT NULL,
    message TEXT NOT NULL,
    date DATE NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY(parent_id) REFERENCES parents(id) ON DELETE CASCADE
);

-- Default Admin User
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');

-- Default Testing Data
INSERT INTO users (username, password, role) VALUES ('faculty1', 'faculty123', 'faculty');
INSERT INTO faculty (employee_id, name, department, user_id) VALUES ('E001', 'Prof. Smith', 'CSE', 2);

INSERT INTO users (username, password, role) VALUES ('student1', 'student123', 'student');
INSERT INTO students (roll_number, name, department, user_id) VALUES ('R001', 'John Doe', 'CSE', 3);

INSERT INTO users (username, password, role) VALUES ('parent1', 'parent123', 'parent');
INSERT INTO parents (name, student_id, user_id) VALUES ('Mr. Doe', 1, 4);

INSERT INTO courses (course_code, course_name, credits) VALUES ('CS101', 'Intro to Programming', 4);
INSERT INTO courses (course_code, course_name, credits) VALUES ('CS102', 'Data Structures', 4);

-- Enroll John in CS101 and CS102
INSERT INTO student_courses (student_id, course_id) VALUES (1, 1), (1, 2);

-- Timetable for Prof. Smith teaching CS101 on Monday
INSERT INTO timetables (course_id, faculty_id, day_of_week, start_time, end_time) VALUES (1, 1, 'Monday', '09:00:00', '10:30:00');

-- Some past attendance for testing
INSERT INTO attendance (date, student_id, course_id, status, marked_by) VALUES ('2023-10-01', 1, 1, 'Present', 1);
INSERT INTO attendance (date, student_id, course_id, status, marked_by) VALUES ('2023-10-02', 1, 1, 'Absent', 1);
INSERT INTO attendance (date, student_id, course_id, status, marked_by) VALUES ('2023-10-08', 1, 1, 'Present', 1);

SELECT 'Database college_erp successfully created!' AS Status;
