import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    JButton viewMyAttendance, logout;
    String studentId;

    public StudentDashboard(String studentId) {
        this.studentId = studentId;
        setTitle("Student Dashboard");
        setLayout(new FlowLayout());

        viewMyAttendance = new JButton("View My Attendance");
        logout = new JButton("Logout");

        add(viewMyAttendance); add(logout);

        viewMyAttendance.addActionListener(e -> new ViewAttendance(true, studentId));
        logout.addActionListener(e -> { dispose(); new LoginFrame(); });

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}