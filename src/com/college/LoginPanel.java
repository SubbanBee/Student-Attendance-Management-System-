package com.college;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton loginBtn;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        
        // Setup modern FlatLaf styled panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Login to College ERP"));
        formPanel.setPreferredSize(new Dimension(350, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        // JPasswordField ensures passwords are shown as * or dots
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        roleCombo = new JComboBox<>(new String[]{"Admin", "Faculty", "Student", "Parent"});
        formPanel.add(roleCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        loginBtn = new JButton("Login");
        // Style main button
        loginBtn.setBackground(new Color(66, 139, 202));
        loginBtn.setForeground(Color.WHITE);
        formPanel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> attemptLogin());

        add(formPanel);
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String selectedRole = roleCombo.getSelectedItem().toString().toLowerCase();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ? AND role = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, selectedRole);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                String role = rs.getString("role");

                int profileId = -1;
                String profileName = "Admin User";

                // Fetch identity relative to role
                if (role.equals("faculty")) {
                    PreparedStatement f_ps = con.prepareStatement("SELECT id, name FROM faculty WHERE user_id = ?");
                    f_ps.setInt(1, userId);
                    ResultSet frs = f_ps.executeQuery();
                    if (frs.next()) { profileId = frs.getInt("id"); profileName = frs.getString("name"); }
                } else if (role.equals("student")) {
                    PreparedStatement s_ps = con.prepareStatement("SELECT id, name FROM students WHERE user_id = ?");
                    s_ps.setInt(1, userId);
                    ResultSet srs = s_ps.executeQuery();
                    if (srs.next()) { profileId = srs.getInt("id"); profileName = srs.getString("name"); }
                } else if (role.equals("parent")) {
                    PreparedStatement p_ps = con.prepareStatement("SELECT id, name FROM parents WHERE user_id = ?");
                    p_ps.setInt(1, userId);
                    ResultSet prs = p_ps.executeQuery();
                    if (prs.next()) { profileId = prs.getInt("id"); profileName = prs.getString("name"); }
                }

                User sessionUser = new User(userId, username, role, profileId, profileName);
                mainFrame.loginUser(sessionUser);
                
                // Clear fields on successful login
                usernameField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials! Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
