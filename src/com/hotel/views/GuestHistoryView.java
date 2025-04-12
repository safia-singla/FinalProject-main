package com.hotel.views;

import com.hotel.database.ReservationDAO;
import com.hotel.models.Reservation;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GuestHistoryView provides an interface to search and view guest reservation history.
 * Displays a table of reservations filtered by guest name.
 */
public class GuestHistoryView extends JFrame {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nameField;

    /**
     * Constructs the GuestHistoryView frame, initializes UI components and layout.
     */
    public GuestHistoryView() {
        setTitle("\uD83D\uDD0E Guest History");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header title
        JLabel title = new JLabel("\uD83D\uDCD6 Guest Reservation History", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Top panel: search bar and buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel nameLabel = new JLabel("Enter Guest Name:");
        nameField = new JTextField(20);
        JButton searchBtn = new JButton("\uD83D\uDD0D Search");
        JButton backBtn = new JButton("\u2190 Back");

        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(searchBtn);
        topPanel.add(backBtn);

        // Combine title and top panel
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(title, BorderLayout.NORTH);
        topContainer.add(topPanel, BorderLayout.SOUTH);

        // Table setup
        String[] columns = {"ID", "Guest Name", "Check-In", "Check-Out", "Room Type", "Status", "Requests"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // Make table fully non-editable
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 15));

        // Action listeners
        searchBtn.addActionListener(e -> performSearch());
        nameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new MainMenuView(LoginView.loggedInUser);
        });

        // Layout arrangement
        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        performSearch(); // Initial load with all reservations
        setVisible(true);
    }

    /**
     * Performs search operation by filtering reservations based on input guest name.
     */
    private void performSearch() {
        String keyword = nameField.getText().trim().toLowerCase();
        List<Reservation> allReservations = new ReservationDAO().getAllReservations();

        List<Reservation> filtered = allReservations.stream()
                .filter(r -> r.getGuestName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        tableModel.setRowCount(0); // Clear existing rows
        for (Reservation r : filtered) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getGuestName(),
                    r.getCheckIn(),
                    r.getCheckOut(),
                    r.getRoomType(),
                    r.getPaymentStatus(),
                    r.getSpecialRequests()
            });
        }
    }

    /**
     * Main method for standalone testing.
     */
    public static void main(String[] args) {
        new GuestHistoryView();
    }
}