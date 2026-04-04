/**
 * Run this to test DB connection - shows exact error in console.
 * java -cp ".;path\to\mysql-connector-j-*.jar" DBTest
 */
public class DBTest {
    public static void main(String[] args) {
        System.out.println("Testing MySQL connection...");
        System.out.println("URL: jdbc:mysql://127.0.0.1:3306/college_erp");
        System.out.println("User: ams_user");
        System.out.println("---");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded OK.");
            String url = "jdbc:mysql://127.0.0.1:3306/college_erp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            java.sql.Connection conn = java.sql.DriverManager.getConnection(url, "ams_user", "password123");
            System.out.println("SUCCESS! Connected to college_erp");
            conn.close();
        } catch (Exception e) {
            System.out.println("FAILED!");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
