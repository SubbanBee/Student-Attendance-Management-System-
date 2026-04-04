import javax.swing.*;
import java.sql.*;

public class ExportPDF extends JFrame {
    public ExportPDF() {
        setTitle("Export Attendance to PDF");
        setSize(400, 200);
        setLayout(null);

        JButton exportBtn = new JButton("Export");
        exportBtn.setBounds(150, 50, 100, 30);
        add(exportBtn);

        exportBtn.addActionListener(e -> exportData());

        setVisible(true);
    }

    private void exportData() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM attendance");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " +
                                   rs.getInt("student_id") + " " +
                                   rs.getInt("subject_id") + " " +
                                   rs.getDate("date") + " " +
                                   rs.getString("status"));
            }
            JOptionPane.showMessageDialog(this, "Data ready to export (check console for now)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}