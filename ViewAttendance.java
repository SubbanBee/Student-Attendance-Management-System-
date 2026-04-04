import javax.swing.*;
import java.sql.*;

public class ViewAttendance extends JFrame {
    public ViewAttendance(boolean showOwnOnly, String userOrRole) {
        setTitle(showOwnOnly ? "My Attendance" : "All Attendance");
        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        add(new JScrollPane(textArea));

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            String query;

            if(showOwnOnly) {
                // For student
                query = "SELECT date, status FROM attendance WHERE student_id='" + userOrRole + "'";
            } else {
                // For faculty or admin, show all students
                query = "SELECT student_id, date, status FROM attendance";
            }

            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                if(showOwnOnly) {
                    textArea.append("Date: " + rs.getString("date") + " | Status: " + rs.getString("status") + "\n");
                } else {
                    textArea.append("Student: " + rs.getString("student_id") +
                                    " | Date: " + rs.getString("date") +
                                    " | Status: " + rs.getString("status") + "\n");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error:\n" + e.getMessage());
        }

        setSize(600, 400);
        setVisible(true);
    }
}