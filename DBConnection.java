import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/college_erp";
    private static final String USER = "ams_user";
    private static final String PASSWORD = "password123"; // change this

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}