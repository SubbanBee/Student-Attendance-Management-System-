package com.college;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminPanel extends JPanel {
    private MainFrame mainFrame;
    private User user;
    private JTabbedPane tabbedPane;

    public AdminPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.user = user;
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.add(new JLabel("<html><h2>Admin Dashboard</h2></html>"), BorderLayout.WEST);
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> mainFrame.logout());
        header.add(logoutBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

        // Add Tabs
        tabbedPane.addTab("Manage Students", createStudentTab());
        tabbedPane.addTab("Manage Faculty", createFacultyTab());
        tabbedPane.addTab("Manage Parents", createParentTab());
        tabbedPane.addTab("Courses & Scheduling", createCourseTab());
        tabbedPane.addTab("Global Attendance", createAttendanceTab());
        tabbedPane.addTab("Notifications", createNotificationTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ==========================================
    // 1. Manange Students Tab
    // ==========================================
    private JPanel createStudentTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form Top
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField rollField = new JTextField(10);
        JTextField nameField = new JTextField(15);
        JTextField deptField = new JTextField(10);
        JButton addBtn = new JButton("Register Student");
        JButton delBtn = new JButton("Remove Student (by Roll)");

        form.add(new JLabel("Roll No:")); form.add(rollField);
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Dept:")); form.add(deptField);
        form.add(addBtn); form.add(delBtn);

        panel.add(form, BorderLayout.NORTH);

        // Table
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Roll No", "Name", "Dept", "User ID"}, 0);
        JTable table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton refreshBtn = new JButton("Refresh Data");
        panel.add(refreshBtn, BorderLayout.SOUTH);

        // Load data lambda
        Runnable loadData = () -> {
            model.setRowCount(0);
            try (Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery("SELECT * FROM students");
                while(rs.next()) {
                    model.addRow(new Object[]{rs.getInt("id"), rs.getString("roll_number"), rs.getString("name"), rs.getString("department"), rs.getInt("user_id")});
                }
            } catch (Exception e) { e.printStackTrace(); }
        };

        refreshBtn.addActionListener(e -> loadData.run());
        
        addBtn.addActionListener(e -> {
            try (Connection c = DBConnection.getConnection()) {
                // 1. Create auth user
                PreparedStatement u_ps = c.prepareStatement("INSERT INTO users(username, password, role) VALUES(?, ?, 'student')", PreparedStatement.RETURN_GENERATED_KEYS);
                u_ps.setString(1, rollField.getText()); u_ps.setString(2, "student123");
                u_ps.executeUpdate();
                ResultSet generatedKeys = u_ps.getGeneratedKeys();
                int userId = generatedKeys.next() ? generatedKeys.getInt(1) : 0;
                
                // 2. Create student
                PreparedStatement ps = c.prepareStatement("INSERT INTO students(roll_number, name, department, user_id) VALUES(?, ?, ?, ?)");
                ps.setString(1, rollField.getText()); ps.setString(2, nameField.getText()); ps.setString(3, deptField.getText()); ps.setInt(4, userId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student added. Default login: " + rollField.getText() + " / student123");
                loadData.run();
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE); }
        });

        delBtn.addActionListener(e -> {
            try (Connection c = DBConnection.getConnection()) {
                PreparedStatement ps = c.prepareStatement("DELETE s, u FROM students s JOIN users u ON s.user_id = u.id WHERE s.roll_number = ?");
                ps.setString(1, rollField.getText());
                int rows = ps.executeUpdate();
                if(rows > 0) JOptionPane.showMessageDialog(this, "Student & Auth record deleted!");
                loadData.run();
            } catch(Exception ex) { ex.printStackTrace(); }
        });

        loadData.run();
        return panel;
    }

    // ==========================================
    // 2. Manage Faculty Tab
    // ==========================================
    private JPanel createFacultyTab() {
        JPanel panel = new JPanel(new BorderLayout());
        // Simple form
        JPanel form = new JPanel(new FlowLayout());
        JTextField empIdField = new JTextField(10);
        JTextField nameField = new JTextField(15);
        JTextField deptField = new JTextField(10);
        JButton addBtn = new JButton("Add Faculty");
        form.add(new JLabel("Emp ID:")); form.add(empIdField);
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Dept:")); form.add(deptField);
        form.add(addBtn);
        panel.add(form, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Emp ID", "Name", "Dept", "User ID"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        Runnable loadData = () -> {
            model.setRowCount(0);
            try (Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery("SELECT * FROM faculty");
                while(rs.next()) {
                    model.addRow(new Object[]{rs.getInt("id"), rs.getString("employee_id"), rs.getString("name"), rs.getString("department"), rs.getInt("user_id")});
                }
            } catch (Exception e) {}
        };
        addBtn.addActionListener(e -> {
            try (Connection c = DBConnection.getConnection()) {
                PreparedStatement u_ps = c.prepareStatement("INSERT INTO users(username, password, role) VALUES(?, ?, 'faculty')", PreparedStatement.RETURN_GENERATED_KEYS);
                u_ps.setString(1, empIdField.getText()); u_ps.setString(2, "faculty123");
                u_ps.executeUpdate();
                ResultSet generatedKeys = u_ps.getGeneratedKeys();
                int userId = generatedKeys.next() ? generatedKeys.getInt(1) : 0;
                
                PreparedStatement ps = c.prepareStatement("INSERT INTO faculty(employee_id, name, department, user_id) VALUES(?, ?, ?, ?)");
                ps.setString(1, empIdField.getText()); ps.setString(2, nameField.getText()); ps.setString(3, deptField.getText()); ps.setInt(4, userId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Faculty Added!");
                loadData.run();
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE); }
        });
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(evt -> loadData.run());
        panel.add(refreshBtn, BorderLayout.SOUTH);

        loadData.run();
        return panel;
    }

    // ==========================================
    // 3. Manage Parents Tab
    // ==========================================
    private JPanel createParentTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(10);
        JTextField studentRollField = new JTextField(10);
        JTextField usernameField = new JTextField(10);
        JButton addBtn = new JButton("Add Parent");
        form.add(new JLabel("Parent Name:")); form.add(nameField);
        form.add(new JLabel("Student Roll No:")); form.add(studentRollField);
        form.add(new JLabel("Username:")); form.add(usernameField);
        form.add(addBtn);
        panel.add(form, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Child Roll No", "Username"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        Runnable loadData = () -> {
            model.setRowCount(0);
            try (Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery(
                    "SELECT p.id, p.name, s.roll_number, u.username FROM parents p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "JOIN students s ON p.student_id = s.id");
                while(rs.next()) {
                    model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("roll_number"), rs.getString("username")});
                }
            } catch (Exception e) {}
        };
        addBtn.addActionListener(e -> {
            try (Connection c = DBConnection.getConnection()) {
                // Find internal student ID by Roll Number
                PreparedStatement findPs = c.prepareStatement("SELECT id FROM students WHERE roll_number = ?");
                findPs.setString(1, studentRollField.getText().trim());
                ResultSet findRs = findPs.executeQuery();
                if (!findRs.next()) {
                    JOptionPane.showMessageDialog(this, "Error: Student with Roll No '" + studentRollField.getText() + "' not found!", "Student Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int studentDbId = findRs.getInt("id");

                PreparedStatement u_ps = c.prepareStatement("INSERT INTO users(username, password, role) VALUES(?, ?, 'parent')", PreparedStatement.RETURN_GENERATED_KEYS);
                u_ps.setString(1, usernameField.getText().trim()); u_ps.setString(2, "parent123");
                u_ps.executeUpdate();
                ResultSet keys = u_ps.getGeneratedKeys();
                if(keys.next()) {
                    PreparedStatement ps = c.prepareStatement("INSERT INTO parents(name, student_id, user_id) VALUES(?, ?, ?)");
                    ps.setString(1, nameField.getText().trim()); ps.setInt(2, studentDbId); ps.setInt(3, keys.getInt(1));
                    ps.executeUpdate();
                    loadData.run();
                    JOptionPane.showMessageDialog(this, "Parent account created successfully!");
                }
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE); }
        });
        loadData.run();
        return panel;
    }

    // ==========================================
    // 4. Courses & Timetables Tab
    // ==========================================
    private JPanel createCourseTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new FlowLayout());
        JTextField codeField = new JTextField(5);
        JTextField nameField = new JTextField(15);
        JButton addCourseBtn = new JButton("Add Course");
        form.add(new JLabel("Code:")); form.add(codeField);
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(addCourseBtn);
        panel.add(form, BorderLayout.NORTH);

        DefaultTableModel cm = new DefaultTableModel(new String[]{"ID", "Code", "Name", "Credits"}, 0);
        JTable tbl = new JTable(cm);
        panel.add(new JScrollPane(tbl), BorderLayout.CENTER);

        Runnable loadData = () -> {
            cm.setRowCount(0);
            try(Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery("SELECT * FROM courses");
                while(rs.next()) cm.addRow(new Object[]{rs.getInt("id"), rs.getString("course_code"), rs.getString("course_name"), rs.getInt("credits")});
            } catch(Exception e){}
        };
        addCourseBtn.addActionListener(e -> {
            try(Connection c = DBConnection.getConnection()) {
                PreparedStatement ps = c.prepareStatement("INSERT INTO courses(course_code, course_name) VALUES(?,?)");
                ps.setString(1, codeField.getText()); ps.setString(2, nameField.getText());
                ps.executeUpdate();
                loadData.run();
            }catch(Exception ex){ JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        loadData.run();
        return panel;
    }

    // ==========================================
    // 5. Global Attendance (Download & Sort) Tab
    // ==========================================
    private JPanel createAttendanceTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        JButton loadAll = new JButton("Load All");
        JButton downloadCSV = new JButton("Download CSV");
        top.add(loadAll); top.add(downloadCSV);
        panel.add(top, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Student Name", "Course", "Status"}, 0);
        JTable table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter); // Enable clicking headers to sort!
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        Runnable load = () -> {
            model.setRowCount(0);
            try(Connection c = DBConnection.getConnection()) {
                ResultSet rs = c.createStatement().executeQuery(
                    "SELECT a.date, s.name as student_name, c.course_name, a.status " +
                    "FROM attendance a " +
                    "JOIN students s ON a.student_id = s.id " +
                    "JOIN courses c ON a.course_id = c.id ORDER BY a.date DESC"
                );
                while(rs.next()) {
                    model.addRow(new Object[]{rs.getString("date"), rs.getString("student_name"), rs.getString("course_name"), rs.getString("status")});
                }
            } catch(Exception e){}
        };
        loadAll.addActionListener(e -> load.run());

        downloadCSV.addActionListener(e -> {
            try {
                FileWriter fw = new FileWriter("Global_Attendance_Report.csv");
                fw.append("Date,Student Name,Course,Status\n");
                for(int i=0; i<table.getRowCount(); i++) {
                    fw.append(table.getValueAt(i,0).toString()).append(",")
                      .append(table.getValueAt(i,1).toString()).append(",")
                      .append(table.getValueAt(i,2).toString()).append(",")
                      .append(table.getValueAt(i,3).toString()).append("\n");
                }
                fw.flush(); fw.close();
                JOptionPane.showMessageDialog(this, "Attendance exported to Global_Attendance_Report.csv!");
            } catch(Exception ex) { ex.printStackTrace(); }
        });

        load.run();
        return panel;
    }

    // ==========================================
    // 6. Notifications (<75%) Tab
    // ==========================================
    private JPanel createNotificationTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        JButton auditBtn = new JButton("Run 75% Audit & Notify Parents");
        top.add(auditBtn);
        panel.add(top, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Student Name", "Parent Name", "Attendance %", "Notified?"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        auditBtn.addActionListener(evt -> {
            model.setRowCount(0);
            try(Connection c = DBConnection.getConnection()) {
                // Find attendance % per student
                String q = "SELECT s.id as sid, s.name as sname, p.id as pid, p.name as pname, " +
                           "SUM(IF(a.status='Present',1,0)) as present_days, " +
                           "COUNT(a.id) as total_days " +
                           "FROM students s " +
                           "LEFT JOIN parents p ON p.student_id = s.id " +
                           "LEFT JOIN attendance a ON a.student_id = s.id " +
                           "GROUP BY s.id, s.name, p.id, p.name";
                ResultSet rs = c.createStatement().executeQuery(q);
                while(rs.next()) {
                    int total = rs.getInt("total_days");
                    if(total == 0) continue; // no attendance recorded
                    int present = rs.getInt("present_days");
                    float percent = (float)present / total * 100;
                    
                    String notified = "No Parent found";
                    int pid = rs.getInt("pid");
                    if (percent < 75.0 && pid > 0) {
                        int sid = rs.getInt("sid");
                        // Insert notification
                        PreparedStatement n_ps = c.prepareStatement(
                            "INSERT INTO notifications(student_id, parent_id, message, date) VALUES(?,?,?,CURDATE())");
                        n_ps.setInt(1, sid); n_ps.setInt(2, pid);
                        n_ps.setString(3, "Warning: Your child's attendance is below 75% (" + String.format("%.2f", percent) + "%).");
                        n_ps.executeUpdate();
                        notified = "Yes (Warning Sent)";
                    } else if (percent >= 75.0) {
                        notified = "Safe";
                    }

                    model.addRow(new Object[]{rs.getString("sname"), rs.getString("pname"), String.format("%.2f%%", percent), notified});
                }
            }catch(Exception ex){ ex.printStackTrace(); }
        });

        return panel;
    }
}
