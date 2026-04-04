import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    JButton addStudent, viewStudents, addFaculty, viewFaculty, viewAttendance, logout;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setLayout(new FlowLayout());

        // Buttons
        addStudent = new JButton("Add Student");
        viewStudents = new JButton("View Students");
        addFaculty = new JButton("Add Faculty");
        viewFaculty = new JButton("View Faculty");
        viewAttendance = new JButton("View Attendance");
        logout = new JButton("Logout");

        add(addStudent); add(viewStudents);
        add(addFaculty); add(viewFaculty);
        add(viewAttendance); add(logout);

        // Action listeners
        addStudent.addActionListener(e -> new AddStudent());
        viewStudents.addActionListener(e -> new ViewUsers("student"));
        addFaculty.addActionListener(e -> new AddFaculty());
        viewFaculty.addActionListener(e -> new ViewUsers("faculty"));
        viewAttendance.addActionListener(e -> new ViewAttendance(false, "Admin"));
        logout.addActionListener(e -> { dispose(); new LoginFrame(); });

        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}