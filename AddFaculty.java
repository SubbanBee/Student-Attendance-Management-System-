import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddFaculty extends JFrame {
    public AddFaculty() {
        setTitle("Add Faculty");
        setLayout(new FlowLayout());

        JLabel idLabel = new JLabel("Faculty ID:");
        JTextField idField = new JTextField(10);
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(10);
        JButton addBtn = new JButton("Add Faculty");

        add(idLabel); add(idField);
        add(nameLabel); add(nameField);
        add(addBtn);

        addBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();

            if(id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO faculty(faculty_id, name) VALUES(?, ?)"
                );
                ps.setString(1, id);
                ps.setString(2, name);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Faculty added successfully!");
                idField.setText(""); nameField.setText("");
            } catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        setSize(300, 150);
        setVisible(true);
    }
}