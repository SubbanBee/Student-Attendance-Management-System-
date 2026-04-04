import javax.swing.*;
import java.sql.*;

public class AttendanceChart extends JFrame {
    public AttendanceChart() {
        setTitle("Attendance Chart");
        setSize(500, 400);
        setLayout(null);

        JTextArea textArea = new JTextArea();
        textArea.setBounds(20, 20, 450, 300);
        add(textArea);

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                textArea.setText("Database connection failed.");
                setVisible(true);
                return;
            }
            String sql = "SELECT s.name AS student, sub.name AS subject, COUNT(*) AS total_present " +
                         "FROM attendance a " +
                         "JOIN students s ON a.student_id = s.id " +
                         "JOIN subjects sub ON a.subject_id = sub.id " +
                         "WHERE a.status='Present' " +
                         "GROUP BY a.student_id, a.subject_id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                textArea.append(rs.getString("student") + " - " +
                                rs.getString("subject") + " : " +
                                rs.getInt("total_present") + " Present\n");
            }
        } catch (SQLException ex) {
            try (Connection conn2 = DBConnection.getConnection()) {
                if (conn2 != null) {
                    ResultSet rs = conn2.createStatement().executeQuery(
                        "SELECT s.name, a.subject, COUNT(*) AS cnt FROM attendance a JOIN students s ON a.student_id=s.id WHERE a.status='Present' GROUP BY a.student_id, a.subject");
                    while (rs.next()) textArea.append(rs.getString(1) + " - " + rs.getString(2) + " : " + rs.getInt(3) + " Present\n");
                }
            } catch (Exception e2) { textArea.setText("Error: " + ex.getMessage()); }
        }
        setVisible(true);
    }
}