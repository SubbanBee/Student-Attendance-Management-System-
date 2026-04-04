import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewFaculty extends JFrame {

    public ViewFaculty() {
        setTitle("All Faculty");
        setSize(500, 400);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Username");

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                setVisible(true);
                return;
            }
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name, username FROM faculty ORDER BY name");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        add(new JScrollPane(table));
        setVisible(true);
    }
}