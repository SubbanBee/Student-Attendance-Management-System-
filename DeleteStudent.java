import javax.swing.*;
import java.sql.*;

public class DeleteStudent extends JFrame {
    private JComboBox<String> studentBox;
    private JButton deleteBtn;

    public DeleteStudent() {
        if (!Session.role.equals("admin")) {
            JOptionPane.showMessageDialog(null, "Access denied. Admin only.", "Unauthorized", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setTitle("Delete Student");
        setSize(300, 200);
        setLayout(null);

        JLabel studentLabel = new JLabel("Student:");
        studentLabel.setBounds(20, 30, 80, 25);
        add(studentLabel);

        studentBox = new JComboBox<>();
        studentBox.setBounds(100, 30, 150, 25);
        add(studentBox);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(100, 70, 100, 25);
        add(deleteBtn);

        deleteBtn.addActionListener(e -> deleteStudent());

        loadStudents();

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

    private void deleteStudent() {
        if (studentBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students in the list.");
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }
            int studentId = Integer.parseInt(studentBox.getSelectedItem().toString().split(" - ")[0]);
            String sql = "DELETE FROM students WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            studentBox.removeItem(studentBox.getSelectedItem());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}