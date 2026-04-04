import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewMarks extends JFrame {

    public ViewMarks(){

        setTitle("Marks");
        setSize(500,400);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Subject");
        model.addColumn("Marks");

        try{

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT subject, marks FROM marks WHERE student_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, Session.userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getString("subject"),
                        rs.getInt("marks")
                });
            }

        }catch(Exception e){

            e.printStackTrace();
        }

        add(new JScrollPane(table));

        setVisible(true);
    }
}