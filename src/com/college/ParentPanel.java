package com.college;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;

public class ParentPanel extends JPanel {
    private MainFrame mainFrame;
    private User user;
    private JTabbedPane tabs;
    private int childStudentId = -1;

    public ParentPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.user = user;
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        header.add(new JLabel("<html><h2>Parent Dashboard - " + user.getProfileName() + "</h2></html>"), BorderLayout.WEST);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> mainFrame.logout());
        header.add(logout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Fetch child student id
        try(Connection c = DBConnection.getConnection()) {
            ResultSet rs = c.createStatement().executeQuery("SELECT student_id, name FROM parents WHERE user_id = " + user.getId());
            if(rs.next()) childStudentId = rs.getInt("student_id");
        } catch(Exception e){}

        if (childStudentId == -1) {
            add(new JLabel("Error: No student linked to your account.", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        tabs = new JTabbedPane();
        tabs.addTab("Child's Attendance", createAttendanceTab());
        tabs.addTab("Notifications Data", createAlertsTab());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createAttendanceTab() {
        JPanel p = new JPanel(new BorderLayout());
        
        DefaultTableModel m = new DefaultTableModel(new String[]{"Date", "Course", "Status"}, 0);
        JTable t = new JTable(m);
        t.setRowSorter(new TableRowSorter<>(m));
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        Runnable load = () -> {
            m.setRowCount(0);
            try(Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery(
                    "SELECT a.date, c.course_name, a.status FROM attendance a " +
                    "JOIN courses c ON a.course_id = c.id " +
                    "WHERE a.student_id = " + childStudentId + " ORDER BY a.date DESC"
                );
                while(rs.next()) {
                    m.addRow(new Object[]{rs.getString("date"), rs.getString("course_name"), rs.getString("status")});
                }
            } catch(Exception e){}
        };

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> load.run());
        p.add(refresh, BorderLayout.SOUTH);
        
        load.run();
        return p;
    }

    private JPanel createAlertsTab() {
        JPanel p = new JPanel(new BorderLayout());
        
        DefaultTableModel m = new DefaultTableModel(new String[]{"Date", "Warning Message"}, 0);
        JTable t = new JTable(m);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        
        Runnable load = () -> {
            m.setRowCount(0);
            try(Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery(
                    "SELECT date, message, is_read FROM notifications WHERE parent_id = " + user.getProfileId() + " ORDER BY date DESC"
                );
                while(rs.next()) {
                    m.addRow(new Object[]{rs.getString("date"), rs.getString("message")});
                }
                c.createStatement().executeUpdate("UPDATE notifications SET is_read=1 WHERE parent_id = " + user.getProfileId());
            } catch(Exception e){}
        };

        JButton refresh = new JButton("Refresh Alerts");
        refresh.addActionListener(e -> load.run());
        p.add(refresh, BorderLayout.SOUTH);
        
        load.run();
        return p;
    }
}
