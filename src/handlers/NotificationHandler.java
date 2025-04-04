package handlers;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationHandler {

    // Method to add a notification for a donor
    public static void addNotification(int donorId, String message) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection failed!");
            return;
        }

        String insertQuery = "INSERT INTO notifications (donor_id, message, is_read) VALUES (?, ?, false)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setInt(1, donorId);
            stmt.setString(2, message);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding notification: " + e.getMessage());
        }
    }

    // Method to fetch notifications for a donor
    public static void viewNotifications(int donorId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection failed!");
            return;
        }

        String query = "SELECT id, message FROM notifications WHERE donor_id = ? AND is_read = false";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, donorId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n=== Your Notifications ===");
            boolean hasNotifications = false;

            while (rs.next()) {
                hasNotifications = true;
                System.out.println("Notification ID: " + rs.getInt("id"));
                System.out.println("Message: " + rs.getString("message"));
                System.out.println("-------------------------");
            }

            if (!hasNotifications) {
                System.out.println("No new notifications.");
            } else {
                markNotificationsAsRead(donorId);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching notifications: " + e.getMessage());
        }
    }

    // Mark notifications as read after viewing
    private static void markNotificationsAsRead(int donorId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            return;
        }

        String updateQuery = "UPDATE notifications SET is_read = true WHERE donor_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, donorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error marking notifications as read: " + e.getMessage());
        }
    }
}
