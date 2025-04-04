package handlers;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentHandler {

    public static boolean bookAppointment(int donorId, String centerName, String appointmentDate) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection failed!");
            return false;
        }

        int centerId = -1;

        // Get the center_id from the centers table
        String getCenterIdQuery = "SELECT id FROM centers WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(getCenterIdQuery)) {
            stmt.setString(1, centerName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    centerId = rs.getInt("id");
                } else {
                    System.out.println("Error: Center not found.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching center ID: " + e.getMessage());
            return false;
        }
        String checkDuplicateQuery = "SELECT id FROM appointments WHERE donor_id = ? AND center_id = ? AND appointment_date = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkDuplicateQuery)) {
            stmt.setInt(1, donorId);
            stmt.setInt(2, centerId);
            stmt.setString(3, appointmentDate);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Error: You already have an appointment at this center on " + appointmentDate);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error checking existing appointment: " + e.getMessage());
            return false;
        }

        // Insert the appointment using center_id
        String insertQuery = "INSERT INTO appointments (donor_id, center_id, appointment_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setInt(1, donorId);
            stmt.setInt(2, centerId);
            stmt.setString(3, appointmentDate);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment successfully booked!");

                // 🔹 Send notification after booking appointment
                NotificationHandler.addNotification(donorId,
                        " Your appointment at " + centerName + " is confirmed for " + appointmentDate + ".");

                return true;
            } else {
                System.out.println("Failed to book appointment. Please try again.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error booking appointment: " + e.getMessage());
            return false;
        }
    }

    public static void viewAppointments(int donorId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection failed!");
            return;
        }

        String query = "SELECT a.id, c.name AS center_name, a.appointment_date " +
                "FROM appointments a " +
                "JOIN centers c ON a.center_id = c.id " +
                "WHERE a.donor_id = ? " +
                "ORDER BY a.appointment_date ASC";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, donorId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n=== Your Appointments ===");
            boolean hasAppointments = false;
            while (rs.next()) {
                hasAppointments = true;
                System.out.println("Appointment ID: " + rs.getInt("id"));
                System.out.println("Center: " + rs.getString("center_name"));
                System.out.println("Date: " + rs.getString("appointment_date"));
                System.out.println("-------------------------");
            }

            if (!hasAppointments) {
                System.out.println("You have no upcoming appointments.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching appointments: " + e.getMessage());
        }
    }


}