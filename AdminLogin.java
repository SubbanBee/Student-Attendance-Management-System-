import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AdminLogin {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM admin WHERE username=? AND password=SHA1(?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                System.out.println("Login Successful ✅");
            }
            else {
                System.out.println("Invalid Credentials ❌");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}