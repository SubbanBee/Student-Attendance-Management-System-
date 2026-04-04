import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn;

    public LoginFrame() {
        setTitle("Login");
        setLayout(new FlowLayout());

        add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        add(passwordField);

        loginBtn = new JButton("Login");
        add(loginBtn);

        loginBtn.addActionListener(e -> login());

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Hardcoded admin login (or you can add Admin to DB)
        if(username.equals("admin") && password.equals("admin123")) {
            dispose();
            new AdminDashboard();
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT role, id FROM users WHERE username=? AND password=?"
            );
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                String role = rs.getString("role");
                String id = rs.getString("id");
                dispose();

                switch(role) {
                    case "Faculty":
                        new FacultyDashboard(id);
                        break;
                    case "Student":
                        new StudentDashboard(id);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Unknown role!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Database error:\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}