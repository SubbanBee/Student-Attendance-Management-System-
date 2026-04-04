import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class ViewStudents extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ViewStudents(){

        setTitle("All Students");
        setSize(500,300);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Roll No");
        model.addColumn("Course");
        model.addColumn("Year");
        model.addColumn("Section");

        add(new JScrollPane(table));

        loadStudents();

        setVisible(true);
    }

    void loadStudents(){

        try{

            Connection con = DBConnection.getConnection();

            String query = "SELECT id,name,roll_no,course,year,section FROM students";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        rs.getString("course"),
                        rs.getInt("year"),
                        rs.getString("section")
                });
            }

        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }
    }
}