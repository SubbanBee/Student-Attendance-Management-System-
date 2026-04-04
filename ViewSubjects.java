import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewSubjects extends JFrame {

    public ViewSubjects(){

        setTitle("Subjects");
        setSize(500,400);

        DefaultTableModel model = new DefaultTableModel();

        JTable table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Subject Name");

        try{

            Connection conn = DBConnection.getConnection();

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM subjects");

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name")
                });
            }

        }catch(Exception e){

            e.printStackTrace();
        }

        add(new JScrollPane(table));

        setVisible(true);
    }
}