import javax.swing.*;
import java.sql.*;

public class AddSubject extends JFrame {
    private JTextField nameField;
    private JButton addBtn;

public AddSubject() {
        if (!Session.role.equals("admin")) {
            JOptionPane.showMessageDialog(null, "Access denied. Admin only.", "Unauthorized", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setTitle("Add Subject");

        setSize(300, 150);
        setLayout(null);

        JLabel nameLabel = new JLabel("Subject Name:");
        nameLabel.setBounds(20, 30, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 30, 120, 25);
        add(nameField);

        addBtn = new JButton("Add");
        addBtn.setBounds(100, 70, 80, 25);
        add(addBtn);

        addBtn.addActionListener(e -> addSubjectToDB());

        setVisible(true);
    }

    private void addSubjectToDB() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter subject name.");
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }
            String sql = "INSERT INTO subjects(name) VALUES(?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Subject added successfully!");
            nameField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}