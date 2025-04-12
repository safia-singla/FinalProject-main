/**
 * Main dashboard view for the Horizon Hotel Management System.
 * Displays role-based navigation for users to access different modules.
 */
package com.hotel.views;

import com.hotel.models.User;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {

    private final User currentUser;

    /**
     * Initializes the main menu for the logged-in user.
     * @param user The logged-in user.
     */
    public MainMenuView(User user) {
        this.currentUser = user;

        setTitle("Horizon Hotel Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar setup for navigation
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(35, 40, 45));
        sidePanel.setPreferredSize(new Dimension(220, 600));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        // Hotel branding and user profile info
        JLabel logo = new JLabel("\uD83C\uDFE8 Horizon Hotel", SwingConstants.CENTER);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel profileIcon = new JLabel("\uD83D\uDC64 " + currentUser.getUsername() + " (" + currentUser.getRole() + ")", SwingConstants.CENTER);
        profileIcon.setForeground(new Color(200, 200, 200));
        profileIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        profileIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        sidePanel.add(logo);
        sidePanel.add(profileIcon);

        // Role-based navigation menu
        String role = currentUser.getRole().toLowerCase();

        if (role.equals("admin") || role.equals("receptionist")) {
            sidePanel.add(navButton("➕ Add Reservation", () -> new ReservationForm(currentUser)));
            sidePanel.add(navButton("📋 View Reservations", ReservationListView::new));
            sidePanel.add(navButton("💳 Billing", BillingView::new));
            sidePanel.add(navButton("🧾 Guest History", GuestHistoryView::new));
        }

        if (role.equals("admin") || role.equals("housekeeping")) {
            sidePanel.add(navButton("🧹 Housekeeping", HousekeepingView::new));
            sidePanel.add(navButton("🛏️ Room Management", RoomManagementView::new));
        }

        if (role.equals("admin")) {
            sidePanel.add(navButton("📦 Inventory", InventoryView::new));
            sidePanel.add(navButton("📊 Reports", () -> new ReportsView(currentUser)));
        }

        // Logout option
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(navButton("🚪 Logout", () -> {
            dispose();
            new LoginView();
        }));

        // Main content welcome panel
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(Color.WHITE);

        JLabel welcome = new JLabel("Welcome to the Horizon Hotel Management System, " + currentUser.getUsername() + "!", SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcome.setForeground(new Color(40, 40, 40));

        mainContent.add(welcome, new GridBagConstraints());

        // Add panels to frame
        add(sidePanel, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Utility method to create a styled sidebar navigation button.
     * @param text Button text.
     * @param action Action to execute on click.
     * @return Configured JButton component.
     */
    private JButton navButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 50, 55));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect for better UX
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 65, 70));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(45, 50, 55));
            }
        });

        button.addActionListener(e -> {
            dispose();
            action.run();
        });

        return button;
    }

    /**
     * Test main method to launch the dashboard with a dummy user.
     */
    public static void main(String[] args) {
        User dummyUser = new User(1, "Tanisha", "admin", "Admin");
        new MainMenuView(dummyUser);
    }
}
