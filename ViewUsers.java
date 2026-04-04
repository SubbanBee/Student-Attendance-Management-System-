import javax.swing.*;
import java.sql.*;

public class ViewUsers extends JFrame {
    public ViewUsers(String type) { // "student" or "faculty"
        setTitle("View " + type + "s");
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        add(new JScrollPane(textArea));

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = type.equals("student") ? "SELECT * FROM students" : "SELECT * FROM faculty";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                if(type.equals("student")) {
                    textArea.append("ID: " + rs.getString("student_id") +
                                    " | Name: " + rs.getString("name") + "\n");
                } else {
                    textArea.append("ID: " + rs.getString("faculty_id") +
                                    " | Name: " + rs.getString("name") + "\n");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }

        setSize(500, 400);
        setVisible(true);
    }
}