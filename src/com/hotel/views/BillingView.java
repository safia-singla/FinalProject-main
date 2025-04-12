package com.hotel.views;

import com.hotel.database.BillDAO;
import com.hotel.database.ReservationDAO;
import com.hotel.database.ServiceDAO;
import com.hotel.models.Bill;
import com.hotel.models.Reservation;
import com.hotel.models.Service;
import com.hotel.utils.PopupUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * BillingView class handles the billing system interface.
 * Allows users to generate and preview bills for individual and group reservations.
 */
public class BillingView extends JFrame {
    private JRadioButton individualBtn, groupBtn;
    private JPanel individualPanel, groupPanel;
    private JComboBox<String> guestBox, groupBox;
    private JTextField individualDiscountField, groupDiscountField;
    private JLabel individualTotalLabel, groupTotalLabel, nightsLabel, roomTypeLabel;
    private JList<String> individualServiceList, groupServiceList;
    private DefaultTableModel tableModel;

    /**
     * Constructor initializes the BillingView UI components.
     */
    public BillingView() {
        setTitle("Billing System");
        setSize(1100, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title label
        JLabel title = new JLabel("\uD83D\uDCB3 Billing System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Toggle Buttons for Individual and Group
        individualBtn = new JRadioButton("Individual", true);
        groupBtn = new JRadioButton("Group");
        ButtonGroup toggleGroup = new ButtonGroup();
        toggleGroup.add(individualBtn);
        toggleGroup.add(groupBtn);

        JPanel togglePanel = new JPanel();
        togglePanel.add(individualBtn);
        togglePanel.add(groupBtn);

        // Card layout for switching between panels
        JPanel centerPanel = new JPanel(new CardLayout());
        individualPanel = buildIndividualPanel();
        groupPanel = buildGroupPanel();
        centerPanel.add(individualPanel, "individual");
        centerPanel.add(groupPanel, "group");

        individualBtn.addActionListener(e -> switchPanel(centerPanel, "individual"));
        groupBtn.addActionListener(e -> switchPanel(centerPanel, "group"));

        // Show default panel
        CardLayout cl = (CardLayout) centerPanel.getLayout();
        cl.show(centerPanel, "individual");

        // Table for displaying bills
        String[] cols = {"ID", "Guest/Group", "Room", "Nights", "Services", "Total"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(table);

        // Form wrapper
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.add(togglePanel, BorderLayout.NORTH);
        formWrapper.add(centerPanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formWrapper, scrollPane);
        splitPane.setResizeWeight(0.35);
        splitPane.setDividerSize(5);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("\u2190 Back to Dashboard");
        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            new MainMenuView(LoginView.loggedInUser);
        });
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.PAGE_END);

        loadBills();
        setVisible(true);
    }

    /**
     * Builds the individual billing panel.
     *
     * @return JPanel configured for individual billing
     */
    private JPanel buildIndividualPanel() {
        JPanel panel = createBasePanel();

        guestBox = new JComboBox<>(new ReservationDAO().getOnlyIndividualGuestNames().toArray(new String[0]));
        guestBox.addActionListener(e -> fillIndividualData());

        roomTypeLabel = new JLabel();
        nightsLabel = new JLabel();
        individualDiscountField = new JTextField("0");

        List<String> services = new ArrayList<>();
        for (Service s : ServiceDAO.getAllServices()) {
            services.add(s.getServiceName());
        }

        individualServiceList = new JList<>(services.toArray(new String[0]));
        individualServiceList.setVisibleRowCount(4);
        individualServiceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        panel.add(makeField("Guest Name:", guestBox));
        panel.add(makeField("Room Type:", roomTypeLabel));
        panel.add(makeField("Nights:", nightsLabel));
        panel.add(makeField("Services Used:", new JScrollPane(individualServiceList)));
        panel.add(makeField("Discount (%):", individualDiscountField));

        JButton genBillBtn = new JButton("\uD83E\uDDFE Generate Bill");
        genBillBtn.addActionListener(this::handleIndividualBill);
        panel.add(genBillBtn);

        JButton previewBtn = new JButton("\uD83E\uDDEA Preview Bill");
        previewBtn.addActionListener(this::previewIndividualBill);
        panel.add(previewBtn);

        individualTotalLabel = new JLabel("Total: $0.00");
        panel.add(individualTotalLabel);

        return panel;
    }

    private JPanel buildGroupPanel() {
        JPanel panel = createBasePanel();

        groupBox = new JComboBox<>(new ReservationDAO().getAllGroupNames().toArray(new String[0]));
        groupDiscountField = new JTextField("0");

        List<String> services = new ArrayList<>();
        for (Service s : ServiceDAO.getAllServices()) {
            services.add(s.getServiceName());
        }

        groupServiceList = new JList<>(services.toArray(new String[0]));
        groupServiceList.setVisibleRowCount(4);
        groupServiceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        panel.add(makeField("Group Name:", groupBox));
        panel.add(makeField("Group Services:", new JScrollPane(groupServiceList)));
        panel.add(makeField("Group Discount (%):", groupDiscountField));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton payFullBtn = new JButton("\uD83D\uDCB0 Pay Full");
        JButton splitBtn = new JButton("\uD83D\uDD00 Split Bill");

        payFullBtn.addActionListener(this::handleGroupBill);
        splitBtn.addActionListener(this::handleSplitBill);

        buttonPanel.add(payFullBtn);
        buttonPanel.add(splitBtn);
        panel.add(buttonPanel);

        JButton previewBtn = new JButton("\uD83E\uDDEA Preview Group Bill");
        previewBtn.addActionListener(this::previewGroupBill);
        panel.add(previewBtn);

        groupTotalLabel = new JLabel("Total: $0.00");
        panel.add(groupTotalLabel);

        return panel;
    }

    /**
     * Populates individual reservation details when a guest is selected.
     */
    private void fillIndividualData() {
        String guest = (String) guestBox.getSelectedItem();
        if (guest == null) return;
        Reservation r = new ReservationDAO().getReservationByGuestName(guest);
        if (r != null) {
            roomTypeLabel.setText(r.getRoomType());
            nightsLabel.setText(String.valueOf((r.getCheckOut().getTime() - r.getCheckIn().getTime()) / (1000 * 60 * 60 * 24)));
        }
    }

    /**
     * Previews the billing breakdown for an individual guest.
     */
    private void previewIndividualBill(ActionEvent e) {
        String guest = (String) guestBox.getSelectedItem();
        if (guest == null) return;
        Reservation r = new ReservationDAO().getReservationByGuestName(guest);
        if (r == null) return;

        int nights = (int) ((r.getCheckOut().getTime() - r.getCheckIn().getTime()) / (1000 * 60 * 60 * 24));
        double base = getBaseCharge(r.getRoomType()) * nights;
        boolean isLate = r.isLateCheckout();
        if (isLate) base += 25;

        List<Service> selected = convertSelectedServices(individualServiceList);
        double service = selected.stream().mapToDouble(Service::getPrice).sum();
        double tax = (base + service) * 0.12;
        double discount = (base + service + tax) * (Double.parseDouble(individualDiscountField.getText()) / 100);
        double total = base + service + tax - discount;

        individualTotalLabel.setText("Total: $" + String.format("%.2f", total));

        StringBuilder breakdown = new StringBuilder("<html><b>Billing Breakdown for " + guest + "</b><br><br>");
        breakdown.append("Room Type: ").append(r.getRoomType()).append("<br>");
        breakdown.append("Nights: ").append(nights).append("<br>");
        breakdown.append("Late Checkout: ").append(isLate ? "Yes (+$25)" : "No").append("<br>");
        breakdown.append("Base: $").append(String.format("%.2f", base)).append("<br>");
        breakdown.append("Services: $").append(String.format("%.2f", service)).append("<br>");
        breakdown.append("Tax (12%): $").append(String.format("%.2f", tax)).append("<br>");
        breakdown.append("Discount: $").append(String.format("%.2f", discount)).append("<br><br>");
        breakdown.append("<b>Total: $").append(String.format("%.2f", total)).append("</b></html>");

        JOptionPane.showMessageDialog(this, new JLabel(breakdown.toString()), "Preview Bill", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Previews the billing breakdown for a group booking.
     */
    private void previewGroupBill(ActionEvent e) {
        String group = (String) groupBox.getSelectedItem();
        if (group == null) return;
        List<Reservation> reservations = new ReservationDAO().getReservationsByGroup(group);

        double baseTotal = 0;
        int totalNights = 0;
        int lateCount = 0;

        for (Reservation r : reservations) {
            int nights = (int) ((r.getCheckOut().getTime() - r.getCheckIn().getTime()) / (1000 * 60 * 60 * 24));
            double base = getBaseCharge(r.getRoomType()) * nights;
            if (r.isLateCheckout()) {
                base += 25;
                lateCount++;
            }
            baseTotal += base;
            totalNights += nights;
        }

        List<Service> services = convertSelectedServices(groupServiceList);
        double service = services.stream().mapToDouble(Service::getPrice).sum();
        double tax = (baseTotal + service) * 0.12;
        double discount = (baseTotal + service + tax) * (Double.parseDouble(groupDiscountField.getText()) / 100);
        double total = baseTotal + service + tax - discount;

        groupTotalLabel.setText("Total: $" + String.format("%.2f", total));

        StringBuilder breakdown = new StringBuilder("<html><b>Group Billing: " + group + "</b><br><br>");
        breakdown.append("Total Nights: ").append(totalNights).append("<br>");
        breakdown.append("Late Checkouts: ").append(lateCount).append(" (+$25 each)<br>");
        breakdown.append("Base: $").append(String.format("%.2f", baseTotal)).append("<br>");
        breakdown.append("Services: $").append(String.format("%.2f", service)).append("<br>");
        breakdown.append("Tax (12%): $").append(String.format("%.2f", tax)).append("<br>");
        breakdown.append("Discount: $").append(String.format("%.2f", discount)).append("<br><br>");
        breakdown.append("<b>Total: $").append(String.format("%.2f", total)).append("</b></html>");

        JOptionPane.showMessageDialog(this, new JLabel(breakdown.toString()), "Preview Group Bill", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles generating the final bill for an individual reservation.
     */
    private void handleIndividualBill(ActionEvent e) {
        try {
            String guest = (String) guestBox.getSelectedItem();
            if (new BillDAO().isBillAlreadyGenerated(guest)) {
                PopupUtil.showError(this, "Bill already generated for this guest!");
                return;
            }

            Reservation r = new ReservationDAO().getReservationByGuestName(guest);
            int nights = (int) ((r.getCheckOut().getTime() - r.getCheckIn().getTime()) / (1000 * 60 * 60 * 24));
            List<Service> selected = convertSelectedServices(individualServiceList);
            double base = getBaseCharge(r.getRoomType()) * nights;
            if (r.isLateCheckout()) base += 25;

            double service = selected.stream().mapToDouble(Service::getPrice).sum();
            double tax = (base + service) * 0.12;
            double discount = (base + service + tax) * (Double.parseDouble(individualDiscountField.getText()) / 100);
            double total = base + service + tax - discount;

            Bill bill = new Bill(guest, r.getRoomType(), nights, selected, base, tax, discount, total);
            if (new BillDAO().addBill(bill)) {
                PopupUtil.showSuccess(this, "Bill added successfully!");
                loadBills();
            }
        } catch (Exception ex) {
            PopupUtil.showError(this, "Error: " + ex.getMessage());
        }
    }

    /**
     * Handles generating the full bill for a group reservation.
     */
    private void handleGroupBill(ActionEvent e) {
        try {
            String group = (String) groupBox.getSelectedItem();
            BillDAO billDAO = new BillDAO();

            // Check if a bill for the selected group is already generated
            if (billDAO.isBillAlreadyGenerated(group)) {
                PopupUtil.showError(this, "Bill already generated for this group!");
                return;
            }

            // Fetch reservations and selected services for the group
            List<Reservation> reservations = new ReservationDAO().getReservationsByGroup(group);
            List<Service> services = convertSelectedServices(groupServiceList);

            // Calculate bill components
            int totalNights = calculateTotalNights(reservations);
            double baseTotal = calculateBaseTotal(reservations);
            double serviceTotal = calculateServiceTotal(services);
            double tax = calculateTax(baseTotal, serviceTotal);
            double discount = calculateDiscount(baseTotal, serviceTotal, tax);
            double total = baseTotal + serviceTotal + tax - discount;

            // Create and save the bill
            Bill bill = new Bill(group, "Group", totalNights, services, baseTotal, tax, discount, total);

            if (billDAO.addBill(bill)) {
                PopupUtil.showSuccess(this, "Group bill added successfully!");
                loadBills();
            }
        } catch (Exception ex) {
            PopupUtil.showError(this, "Error: " + ex.getMessage());
        }
    }

    /**
     * Handles splitting the total bill among group members.
     */
    private void handleSplitBill(ActionEvent e) {
        try {
            String group = (String) groupBox.getSelectedItem();
            if (group == null) return;

            // Fetch reservations for the group
            List<Reservation> reservations = new ReservationDAO().getReservationsByGroup(group);
            int numGuests = reservations.size();
            if (numGuests == 0) {
                PopupUtil.showError(this, "No guests found in this group.");
                return;
            }

            // Calculate bill components
            List<Service> services = convertSelectedServices(groupServiceList);
            double baseTotal = calculateBaseTotal(reservations);
            double serviceTotal = calculateServiceTotal(services);
            double tax = calculateTax(baseTotal, serviceTotal);
            double discount = calculateDiscount(baseTotal, serviceTotal, tax);
            double total = baseTotal + serviceTotal + tax - discount;
            double perPerson = total / numGuests;

            // Build and display the split bill breakdown
            String breakdown = buildBillBreakdown(group, numGuests, total, perPerson);

            JOptionPane.showMessageDialog(this, new JLabel(breakdown), "Split Bill", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            PopupUtil.showError(this, "Error: " + ex.getMessage());
        }
    }

    /**
     * Converts selected service names from the list to Service objects.
     */
    private List<Service> convertSelectedServices(JList<String> list) {
        List<Service> selectedServices = new ArrayList<>();
        for (String name : list.getSelectedValuesList()) {
            selectedServices.add(ServiceDAO.getServiceByName(name));
        }
        return selectedServices;
    }

    /**
     * Calculates the total base charge for reservations.
     */
    private double calculateBaseTotal(List<Reservation> reservations) {
        return reservations.stream()
                .mapToDouble(r -> {
                    int nights = calculateNights(r);
                    double baseCharge = getBaseCharge(r.getRoomType()) * nights;
                    if (r.isLateCheckout()) baseCharge += 25;
                    return baseCharge;
                })
                .sum();
    }

    /**
     * Calculates the total number of nights for all reservations.
     */
    private int calculateTotalNights(List<Reservation> reservations) {
        return reservations.stream()
                .mapToInt(this::calculateNights)
                .sum();
    }

    /**
     * Calculates the number of nights for a single reservation.
     */
    private int calculateNights(Reservation reservation) {
        long duration = reservation.getCheckOut().getTime() - reservation.getCheckIn().getTime();
        return (int) (duration / (1000 * 60 * 60 * 24));
    }

    /**
     * Calculates the total service charges.
     */
    private double calculateServiceTotal(List<Service> services) {
        return services.stream().mapToDouble(Service::getPrice).sum();
    }

    /**
     * Calculates the tax amount.
     */
    private double calculateTax(double baseTotal, double serviceTotal) {
        return (baseTotal + serviceTotal) * 0.12;
    }

    /**
     * Calculates the discount amount based on user input.
     */
    private double calculateDiscount(double baseTotal, double serviceTotal, double tax) {
        double discountRate = Double.parseDouble(groupDiscountField.getText()) / 100;
        return (baseTotal + serviceTotal + tax) * discountRate;
    }

    /**
     * Builds the breakdown text for the split bill dialog.
     */
    private String buildBillBreakdown(String group, int members, double total, double perPerson) {
        return String.format(
                "<html><b>Split Bill for Group: %s</b><br><br>Members: %d<br>Total Bill: $%.2f<br>Each Pays: <b>$%.2f</b></html>",
                group, members, total, perPerson
        );
    }

    /**
     * Retrieves the base charge for a given room type, adjusting for peak season.
     */
    private double getBaseCharge(String roomType) {
        double multiplier = isPeakSeason() ? 1.2 : 1.0;
        return switch (roomType) {
            case "Deluxe" -> 120 * multiplier;
            case "Suite" -> 180 * multiplier;
            case "Executive" -> 250 * multiplier;
            default -> 80 * multiplier;
        };
    }

    /**
     * Determines if the current month falls within the peak season.
     */
    private boolean isPeakSeason() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return month == 7 || month == 8 || month == 12;
    }

    /**
     * Creates a base panel with vertical layout and padding.
     */
    private JPanel createBasePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    /**
     * Creates a panel for a labeled input field.
     */
    private JPanel makeField(String label, JComponent input) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(input, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(300, 70));

        return panel;
    }

    /**
     * Switches the visible panel in a parent container.
     */
    private void switchPanel(JPanel parent, String name) {
        ((CardLayout) parent.getLayout()).show(parent, name);
    }

    /**
     * Loads all bills into the table model for display.
     */
    private void loadBills() {
        tableModel.setRowCount(0);

        for (Bill bill : new BillDAO().getAllBills()) {
            String services = bill.getServices().stream()
                    .map(Service::getServiceName)
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("");

            tableModel.addRow(new Object[]{
                    bill.getId(),
                    bill.getGuestName(),
                    bill.getRoomType(),
                    bill.getNights(),
                    services,
                    "$" + String.format("%.2f", bill.getTotalAmount())
            });
        }
    }
}