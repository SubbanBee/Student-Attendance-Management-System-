import javax.swing.*;
import java.sql.*;

public class AssignSubject extends JFrame {
    private JComboBox<String> studentBox, subjectBox;
    private JButton assignBtn;

    public AssignSubject() {
        setTitle("Assign Subject");
        setSize(400, 250);
        setLayout(null);

        JLabel studentLabel = new JLabel("Student:");
        studentLabel.setBounds(20, 30, 80, 25);
        add(studentLabel);

        studentBox = new JComboBox<>();
        studentBox.setBounds(120, 30, 200, 25);
        add(studentBox);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setBounds(20, 70, 80, 25);
        add(subjectLabel);

        subjectBox = new JComboBox<>();
        subjectBox.setBounds(120, 70, 200, 25);
        add(subjectBox);

        assignBtn = new JButton("Assign");
        assignBtn.setBounds(150, 120, 80, 30);
        add(assignBtn);

        assignBtn.addActionListener(e -> assignSubject());

        loadStudents();
        loadSubjects();

        setVisible(true);
    }

    private void loadStudents() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name FROM students ORDER BY name");
            while (rs.next()) {
                studentBox.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadSubjects() {
        try (Connection conn = DBConnection.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name FROM subjects");
            while (rs.next()) {
                subjectBox.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void assignSubject() {
        if (studentBox.getItemCount() == 0 || subjectBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Add students and subjects first.");
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }
            int studentId = Integer.parseInt(studentBox.getSelectedItem().toString().split(" - ")[0]);
            int subjectId = Integer.parseInt(subjectBox.getSelectedItem().toString().split(" - ")[0]);

            String sql = "INSERT INTO student_subject(student_id, subject_id) VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Subject assigned successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}