import javax.swing.*;
import java.awt.*;

public class FacultyDashboard extends JFrame {
    JButton markAttendance, viewAttendance, logout;
    String facultyId;

    public FacultyDashboard(String facultyId) {
        this.facultyId = facultyId;
        setTitle("Faculty Dashboard");
        setLayout(new FlowLayout());

        markAttendance = new JButton("Mark Attendance");
        viewAttendance = new JButton("View All Attendance");
        logout = new JButton("Logout");

        add(markAttendance); add(viewAttendance); add(logout);

        // Open mark attendance window with faculty ID
        markAttendance.addActionListener(e -> new MarkAttendance(facultyId));

        // View all attendance (faculty can see all students)
        viewAttendance.addActionListener(e -> new ViewAttendance(false, "Faculty"));

        logout.addActionListener(e -> { dispose(); new LoginFrame(); });

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}