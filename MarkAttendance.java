import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MarkAttendance extends JFrame {
    public MarkAttendance(String facultyId) {
        setTitle("Mark Attendance");
        setLayout(new FlowLayout());

        JLabel label = new JLabel("Student ID:");
        JTextField studentField = new JTextField(10);
        JLabel statusLabel = new JLabel("Status (Present/Absent):");
        JTextField statusField = new JTextField(10);
        JButton submitBtn = new JButton("Submit");

        add(label); add(studentField);
        add(statusLabel); add(statusField);
        add(submitBtn);

        submitBtn.addActionListener(e -> {
            String studentId = studentField.getText().trim();
            String status = statusField.getText().trim();

            if(studentId.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO attendance(student_id, faculty_id, date, status) VALUES(?, ?, CURDATE(), ?)"
                );
                ps.setString(1, studentId);
                ps.setString(2, facultyId);
                ps.setString(3, status);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Attendance marked successfully!");
                studentField.setText(""); statusField.setText("");
            } catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage());
            }
        });

        setSize(400, 200);
        setVisible(true);
    }
}