# Fix Invalid Credentials - Step-by-Step Plan

## Plan Summary
Invalid Login means DB connects but no matching user/hash in `users` table. Defaults: admin/admin123.

## 1. Test DB Connection [PENDING]
Compile & run DBTest to check if MySQL/ams_user works:
```
javac -cp mysql-connector-j-9.6.0.jar;*.java DBTest.java
java -cp .;mysql-connector-j-9.6.0.jar DBTest
```
Expected: SUCCESS → proceed to 2. FAIL → setup MySQL/ams_user.

## 2. Setup DB [PENDING]
- MySQL running? (services.msc → MySQL → Start)
- Run as root: `mysql -u root -p < create_fresh_database.sql`
- Verify: `mysql -u ams_user -ppassword123 college_erp -e "SELECT * FROM users"`

## 3. Test App [PENDING]
```
java -cp .;mysql-connector-j-9.6.0.jar LoginFrame
```
Login: admin / admin123

## 4. Polish [PENDING]
- Better error msgs in LoginFrame.java
- Update run_attendance.bat JAR path
