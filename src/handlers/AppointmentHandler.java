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

        String insertQuery = "INSERT INTO appointments (donor_id, center_id, appointment_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setInt(1, donorId);
            stmt.setInt(2, centerId);
            stmt.setString(3, appointmentDate);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment successfully booked!");

                String confirmationMessage = "Your appointment at " + centerName + " is confirmed for " + appointmentDate + ".";
                NotificationHandler.sendNotification(donorId, confirmationMessage);

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
}
