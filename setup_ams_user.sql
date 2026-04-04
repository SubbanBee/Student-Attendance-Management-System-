-- Run as root to grant ams_user full access to college_erp
-- Command: mysql -u root -p < setup_ams_user.sql

CREATE USER IF NOT EXISTS 'ams_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON college_erp.* TO 'ams_user'@'localhost';
FLUSH PRIVILEGES;
