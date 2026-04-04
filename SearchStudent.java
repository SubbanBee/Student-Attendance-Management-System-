import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SearchStudent extends JFrame {

    JTextField searchField;
    JButton searchButton;
    JTable table;

    public SearchStudent() {
        setTitle("Search Student");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        top.add(new JLabel("Student Name or Roll No:"));
        top.add(searchField);
        top.add(searchButton);

        DefaultTableModel model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Roll No");
        model.addColumn("Course");
        model.addColumn("Year");
        model.addColumn("Section");
        model.addColumn("Username");

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        searchButton.addActionListener(e -> searchStudent());

        setVisible(true);
    }

    void searchStudent() {
        String keyword = searchField.getText();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT id, name, roll_no, course, year, section, username FROM students WHERE name LIKE ? OR roll_no LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // clear previous

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        rs.getString("course"),
                        rs.getInt("year"),
                        rs.getString("section"),
                        rs.getString("username")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}