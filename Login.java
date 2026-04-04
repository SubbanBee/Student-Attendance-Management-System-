import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    JTextField user;
    JPasswordField pass;

    public Login(){

        setTitle("Login");
        setSize(300,200);
        setLayout(new GridLayout(3,2));

        add(new JLabel("Username"));
        user = new JTextField();
        add(user);

        add(new JLabel("Password"));
        pass = new JPasswordField();
        add(pass);

        JButton loginBtn = new JButton("Login");
        add(loginBtn);

        loginBtn.addActionListener(e -> login());

        setVisible(true);
    }

    void login(){

        if(user.getText().equals("admin") &&
           String.valueOf(pass.getPassword()).equals("admin")){

            new AdminDashboard();
            dispose();

        }else{

            JOptionPane.showMessageDialog(this,"Invalid Login");

        }

    }

    public static void main(String[] args){

        new Login();

    }
}