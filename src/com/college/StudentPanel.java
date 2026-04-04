package com.college;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;

public class StudentPanel extends JPanel {
    private MainFrame mainFrame;
    private User user;
    private JTabbedPane tabs;

    public StudentPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.user = user;
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        header.add(new JLabel("<html><h2>Student Dashboard - " + user.getProfileName() + "</h2></html>"), BorderLayout.WEST);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> mainFrame.logout());
        header.add(logout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.addTab("My Attendance", createAttendanceTab());
        tabs.addTab("My Warnings", createWarningsTab());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createAttendanceTab() {
        JPanel p = new JPanel(new BorderLayout());
        
        DefaultTableModel m = new DefaultTableModel(new String[]{"Date", "Course", "Status"}, 0);
        JTable t = new JTable(m);
        t.setRowSorter(new TableRowSorter<>(m));
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        
        JPanel bottom = new JPanel();
        JLabel statsLabel = new JLabel("Calculated %: 0.0%");
        bottom.add(statsLabel);
        p.add(bottom, BorderLayout.SOUTH);

        Runnable load = () -> {
            m.setRowCount(0);
            int presets = 0;
            int totals = 0;
            try(Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery(
                    "SELECT a.date, c.course_name, a.status FROM attendance a " +
                    "JOIN courses c ON a.course_id = c.id " +
                    "WHERE a.student_id = " + user.getProfileId() + " ORDER BY a.date DESC"
                );
                while(rs.next()) {
                    String status = rs.getString("status");
                    m.addRow(new Object[]{rs.getString("date"), rs.getString("course_name"), status});
                    totals++;
                    if("Present".equals(status)) presets++;
                }
                if(totals > 0) {
                    float perc = (float)presets / totals * 100;
                    statsLabel.setText("Overall Attendance: " + String.format("%.2f%%", perc));
                }
            } catch(Exception e){}
        };

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> load.run());
        bottom.add(refresh);
        
        load.run();
        return p;
    }

    private JPanel createWarningsTab() {
        JPanel p = new JPanel(new BorderLayout());
        
        DefaultTableModel m = new DefaultTableModel(new String[]{"Date", "Warning Message", "Read Status"}, 0);
        JTable t = new JTable(m);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        
        Runnable load = () -> {
            m.setRowCount(0);
            try(Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery(
                    "SELECT date, message, is_read FROM notifications WHERE student_id = " + user.getProfileId() + " ORDER BY date DESC"
                );
                while(rs.next()) {
                    m.addRow(new Object[]{rs.getString("date"), rs.getString("message"), rs.getBoolean("is_read") ? "Seen" : "New"});
                }
                
                // Mark as read 
                c.createStatement().executeUpdate("UPDATE notifications SET is_read=1 WHERE student_id = " + user.getProfileId());
            } catch(Exception e){}
        };
        
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> load.run());
        p.add(refresh, BorderLayout.SOUTH);
        
        load.run();
        return p;
    }
}
