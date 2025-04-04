package handlers;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CenterHandler {
    public static void findNearbyCenters(String location) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection failed!");
            return;
        }

        String query = "SELECT name, location FROM centers WHERE location LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + location + "%"); // Search for partial matches
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n=== Nearby Donation Centers ===");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Center: " + rs.getString("name"));
                System.out.println("Location: " + rs.getString("location"));
                System.out.println("----------------------");
            }
            if (!found) {
                System.out.println("No nearby donation centers found.");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching centers: " + e.getMessage());
        }
    }
}

