    package com.hotel.database;

    import com.hotel.models.Bill;
    import com.hotel.models.Service;

    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;

    public class BillDAO {

        /**
        * Adds a new bill to the database.
        * 
        * @param bill the Bill object to be added
        * @return true if the bill was successfully added, false otherwise
        */
        public boolean addBill(Bill bill) {
            String sql = "INSERT INTO bills (guest_name, room_type, nights, services, base_charge, tax, discount, total_amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DBConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Convert List<Service> to a comma-separated String of service names
                String services = convertServicesToString(bill.getServices());

                stmt.setString(1, bill.getGuestName());
                stmt.setString(2, bill.getRoomType());
                stmt.setInt(3, bill.getNights());
                stmt.setString(4, services);
                stmt.setDouble(5, bill.getBaseCharge());
                stmt.setDouble(6, bill.getTax());
                stmt.setDouble(7, bill.getDiscount());
                stmt.setDouble(8, bill.getTotalAmount());

                return stmt.executeUpdate() > 0;

            } catch (SQLException e) {
                System.out.println("Error adding bill: " + e.getMessage());
                return false;
            }
        }

        /**
        * Converts a list of services to a comma-separated string of service names.
        * 
        * @param services list of Service objects
        * @return a string of service names separated by commas
        */
        private String convertServicesToString(List<Service> services) {
            if (services == null || services.isEmpty()) {
                return "";
            }

            StringBuilder servicesString = new StringBuilder();
            for (Service service : services) {
                servicesString.append(service.getServiceName()).append(", ");  // Only service name for simplicity
            }

            // Remove the trailing comma and space
            return servicesString.length() > 0 ? servicesString.substring(0, servicesString.length() - 2) : "";
        }

        /**
        * Checks if a bill has already been generated for the given guest.
        * 
        * @param guestName the name of the guest
        * @return true if a bill already exists, false otherwise
        */
        public boolean isBillAlreadyGenerated(String guestName) {
            String sql = "SELECT COUNT(*) FROM bills WHERE guest_name = ?";
            try (Connection conn = DBConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, guestName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                System.out.println("Error checking if bill already exists: " + e.getMessage());
            }
            return false;
        }

        /**
        * Retrieves all bills from the database.
        * 
        * @return a list of Bill objects
        */
        public List<Bill> getAllBills() {
            List<Bill> list = new ArrayList<>();
            String sql = "SELECT * FROM bills";

            try (Connection conn = DBConnection.getInstance();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Bill bill = new Bill(
                            rs.getInt("id"),
                            rs.getString("guest_name"),
                            rs.getString("room_type"),
                            rs.getInt("nights"),
                            convertStringToServices(rs.getString("services")), // Convert stored string to list of Service objects
                            rs.getDouble("base_charge"),
                            rs.getDouble("tax"),
                            rs.getDouble("discount"),
                            rs.getDouble("total_amount")
                    );
                    list.add(bill);
                }

            } catch (SQLException e) {
                System.out.println("Error fetching bills: " + e.getMessage());
            }

            return list;
        }

        /**
        * Converts a comma-separated string of service names back to a list of Service objects.
        * 
        * @param services a string of service names separated by commas
        * @return a list of Service objects
        */
        private List<Service> convertStringToServices(String services) {
            List<Service> serviceList = new ArrayList<>();
            if (services != null && !services.isEmpty()) {
                String[] serviceArray = services.split(", ");
                for (String serviceName : serviceArray) {
                    // Retrieve the full service details using ServiceDAO
                    serviceList.add(ServiceDAO.getServiceByName(serviceName.trim()));
                }
            }
            return serviceList;
        }
    }
