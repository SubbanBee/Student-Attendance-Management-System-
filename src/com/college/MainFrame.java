package com.college;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private User currentUser;

    public MainFrame() {
        setTitle("College Attendance Management System");
        setSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Add core screens
        mainContainer.add(new LoginPanel(this), "Login");

        add(mainContainer);
        
        // Show login initially
        cardLayout.show(mainContainer, "Login");
    }

    /*
     * Switches the view to the specified dashboard and passes the user object.
     */
    public void loginUser(User user) {
        this.currentUser = user;
        String role = user.getRole();
        
        // create the specific dashboard on-the-fly to load fresh data
        switch (role.toLowerCase()) {
            case "admin":
                mainContainer.add(new AdminPanel(this, user), "AdminDash");
                cardLayout.show(mainContainer, "AdminDash");
                break;
            case "faculty":
                mainContainer.add(new FacultyPanel(this, user), "FacultyDash");
                cardLayout.show(mainContainer, "FacultyDash");
                break;
            case "student":
                mainContainer.add(new StudentPanel(this, user), "StudentDash");
                cardLayout.show(mainContainer, "StudentDash");
                break;
            case "parent":
                mainContainer.add(new ParentPanel(this, user), "ParentDash");
                cardLayout.show(mainContainer, "ParentDash");
                break;
        }
    }

    public void logout() {
        this.currentUser = null;
        cardLayout.show(mainContainer, "Login");
    }
}
