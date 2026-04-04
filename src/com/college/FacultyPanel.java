package com.college;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FacultyPanel extends JPanel {
    private MainFrame mainFrame;
    private User user;
    private JTabbedPane tabs;

    public FacultyPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.user = user;
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        header.add(new JLabel("<html><h2>Faculty Dashboard - " + user.getProfileName() + "</h2></html>"), BorderLayout.WEST);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> mainFrame.logout());
        header.add(logout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.addTab("Mark & Edit Attendance", createMarkAttendanceTab());
        tabs.addTab("View & Sort Attendance", createViewAttendanceTab());
        tabs.addTab("75% Defaulters", createDefaultersTab());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createMarkAttendanceTab() {
        JPanel p = new JPanel(new BorderLayout());
        
        JPanel pnl = new JPanel(new FlowLayout());
        JTextField dateField = new JTextField(10);
        dateField.setText(java.time.LocalDate.now().toString()); // default today
        JTextField rollField = new JTextField(10);
        JComboBox<String> courseCombo = new JComboBox<>();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Late"});
        JButton markBtn = new JButton("Mark/Edit Attendance");

        pnl.add(new JLabel("Date (YYYY-MM-DD):")); pnl.add(dateField);
        pnl.add(new JLabel("Course ID:")); pnl.add(courseCombo); // In a real app we'd load these from DB
        pnl.add(new JLabel("Student Roll:")); pnl.add(rollField);
        pnl.add(new JLabel("Status:")); pnl.add(statusCombo);
        pnl.add(markBtn);

        // Load all available courses
        try(Connection c = DBConnection.getConnection()) {
            ResultSet rs = c.createStatement().executeQuery("SELECT id, course_code FROM courses");
            while(rs.next()) courseCombo.addItem(rs.getString("id") + " - " + rs.getString("course_code"));
        } catch(Exception e){}
        
        DefaultTableModel m = new DefaultTableModel(new String[]{"ID","Date","Course","Student Roll","Status"}, 0);
        JTable t = new JTable(m);
        p.add(pnl, BorderLayout.NORTH);
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        Runnable refresh = () -> {
            m.setRowCount(0);
            try(Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery(
                    "SELECT a.id, a.date, c.course_code, s.roll_number, a.status FROM attendance a " +
                    "JOIN courses c ON a.course_id=c.id " +
                    "JOIN students s ON a.student_id=s.id WHERE a.marked_by=" + user.getProfileId() + " ORDER BY a.date DESC LIMIT 50"
                );
                while(rs.next()) m.addRow(new Object[]{rs.getInt("id"), rs.getString("date"), rs.getString("course_code"), rs.getString("roll_number"), rs.getString("status")});
            } catch(Exception e){}
        };

        markBtn.addActionListener(e -> {
            try(Connection c = DBConnection.getConnection()) {
                // Find student ID
                PreparedStatement sps = c.prepareStatement("SELECT id FROM students WHERE roll_number=?");
                sps.setString(1, rollField.getText());
                ResultSet srs = sps.executeQuery();
                if(!srs.next()) { JOptionPane.showMessageDialog(this, "Student Not Found!"); return; }
                int sid = srs.getInt("id");
                
                String selectedCourse = (String)courseCombo.getSelectedItem();
                if (selectedCourse == null) {
                    JOptionPane.showMessageDialog(this, "No courses are available in the system. Ask Admin to create courses first.", "Missing Data", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int cid = Integer.parseInt(selectedCourse.split(" - ")[0]);

                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO attendance(date, student_id, course_id, status, marked_by) VALUES(?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE status=VALUES(status), marked_by=VALUES(marked_by)"
                );
                ps.setString(1, dateField.getText());
                ps.setInt(2, sid); ps.setInt(3, cid);
                ps.setString(4, statusCombo.getSelectedItem().toString());
                ps.setInt(5, user.getProfileId());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Attendance Saved!");
                refresh.run();
            }catch(Exception ex){ JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        refresh.run();
        return p;
    }

    private JPanel createViewAttendanceTab() {
        JPanel p = new JPanel(new BorderLayout());
        
        JPanel top = new JPanel(new FlowLayout());
        JTextField filterRoll = new JTextField(10);
        JButton filterBtn = new JButton("Filter by Roll");
        JButton dlBtn = new JButton("Download CSV");
        top.add(new JLabel("Roll No:")); top.add(filterRoll);
        top.add(filterBtn); top.add(dlBtn);
        p.add(top, BorderLayout.NORTH);

        DefaultTableModel m = new DefaultTableModel(new String[]{"Date", "Student Name", "Roll No", "Course", "Status"}, 0);
        JTable t = new JTable(m);
        t.setRowSorter(new TableRowSorter<>(m));
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        Runnable load = () -> {
            m.setRowCount(0);
            try(Connection c = DBConnection.getConnection()) {
                String q = "SELECT a.date, s.name, s.roll_number, c.course_name, a.status FROM attendance a " +
                           "JOIN students s ON a.student_id=s.id JOIN courses c ON a.course_id=c.id";
                if(!filterRoll.getText().isEmpty()) {
                    q += " WHERE s.roll_number='" + filterRoll.getText() + "'"; 
                }
                ResultSet rs = c.createStatement().executeQuery(q);
                while(rs.next()) {
                    m.addRow(new Object[]{rs.getString("date"), rs.getString("name"), rs.getString("roll_number"), rs.getString("course_name"), rs.getString("status")});
                }
            }catch(Exception e){}
        };

        filterBtn.addActionListener(e -> load.run());
        dlBtn.addActionListener(e -> {
            try {
                FileWriter fw = new FileWriter("Faculty_Class_Attendance.csv");
                fw.append("Date,Student Name,Roll No,Course,Status\n");
                for(int i=0; i<t.getRowCount(); i++) {
                    fw.append(t.getValueAt(i,0).toString()).append(",")
                      .append(t.getValueAt(i,1).toString()).append(",")
                      .append(t.getValueAt(i,2).toString()).append(",")
                      .append(t.getValueAt(i,3).toString()).append(",")
                      .append(t.getValueAt(i,4).toString()).append("\n");
                }
                fw.flush(); fw.close();
                JOptionPane.showMessageDialog(this, "Attendance downloaded to Faculty_Class_Attendance.csv");
            } catch(Exception ex) {}
        });

        load.run();
        return p;
    }

    private JPanel createDefaultersTab() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel m = new DefaultTableModel(new String[]{"Student Name", "Roll No", "Course", "Attendance %"}, 0);
        JTable t = new JTable(m);
        t.setRowSorter(new TableRowSorter<>(m));
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        JButton loadDef = new JButton("Fetch Defaulters (<75%)");
        p.add(loadDef, BorderLayout.NORTH);

        loadDef.addActionListener(e -> {
            m.setRowCount(0);
            try(Connection c = DBConnection.getConnection()) {
                String q = "SELECT s.name, s.roll_number, crs.course_name, " +
                           "SUM(IF(a.status='Present',1,0)) as P, COUNT(a.id) as T " +
                           "FROM students s JOIN attendance a ON a.student_id=s.id " +
                           "JOIN courses crs ON a.course_id=crs.id " +
                           "GROUP BY s.id, crs.id";
                ResultSet rs = c.createStatement().executeQuery(q);
                while(rs.next()) {
                    int tot = rs.getInt("T");
                    if(tot > 0) {
                        float perc = (float)rs.getInt("P") / tot * 100;
                        if(perc < 75.0f) {
                            m.addRow(new Object[]{rs.getString("name"), rs.getString("roll_number"), rs.getString("course_name"), String.format("%.2f%%", perc)});
                        }
                    }
                }
            }catch(Exception ex){}
        });

        return p;
    }
}
