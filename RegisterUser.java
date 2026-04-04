import javax.swing.*;
import java.sql.*;

public class RegisterUser extends JFrame {

    JTextField userField;
    JPasswordField passField;
    JComboBox<String> roleBox;

    public RegisterUser(){

        setTitle("Register User");
        setSize(300,220);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        JLabel roleLabel = new JLabel("Role");

        userField = new JTextField();
        passField = new JPasswordField();

        roleBox = new JComboBox<>(new String[]{"admin","faculty","student"});

        JButton register = new JButton("Register");

        userLabel.setBounds(30,30,80,25);
        passLabel.setBounds(30,70,80,25);
        roleLabel.setBounds(30,110,80,25);

        userField.setBounds(120,30,120,25);
        passField.setBounds(120,70,120,25);
        roleBox.setBounds(120,110,120,25);

        register.setBounds(90,150,100,30);

        add(userLabel);
        add(passLabel);
        add(roleLabel);
        add(userField);
        add(passField);
        add(roleBox);
        add(register);

        register.addActionListener(e -> registerUser());

        setVisible(true);
    }

    void registerUser(){

        try{

            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO users(username,password,role) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,userField.getText());
ps.setString(2,PasswordUtil.hashPassword(new String(passField.getPassword())));

            ps.setString(3,roleBox.getSelectedItem().toString());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"User Registered");

        }catch(Exception e){

            e.printStackTrace();
        }
    }
}