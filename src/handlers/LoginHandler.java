package handlers;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LoginHandler {
    public static int getDonorId(String username, String password) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection failed!");
            return -1;
        }

        String query = "SELECT id FROM donors WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int donorId = rs.getInt("id");

                // ✅ Pass the existing connection instead of creating a new one
                checkUpcomingAppointment(donorId, conn);
                return donorId;
            } else {
                System.out.println("Invalid username or password.");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
            return -1;
        }
    }

    public static void checkUpcomingAppointment(int donorId, Connection conn) {
        String query = "SELECT c.name, a.appointment_date " +
                "FROM appointments a " +
                "JOIN centers c ON a.center_id = c.id " +
                "WHERE a.donor_id = ? AND a.appointment_date = CURDATE() + INTERVAL 1 DAY";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, donorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("REMINDER: You have an appointment tomorrow!");
                System.out.println("Center: " + rs.getString("name"));
                System.out.println("Date: " + rs.getString("appointment_date"));
                System.out.println("-------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error checking appointments: " + e.getMessage());
        }
    }

        public static String getPhoneNumber(int donorId) {
            String phoneNumber = null;
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT phone_number FROM donors WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, donorId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    phoneNumber = rs.getString("phone_number");
                }
            } catch (Exception e) {
                System.out.println("Error retrieving phone number: " + e.getMessage());
            }
            return phoneNumber;
        }
}

