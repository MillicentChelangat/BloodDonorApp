package handlers;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DonorSearchHandler {

    public static void searchDonors() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nEnter the blood type you are looking for (e.g., A+, O-, etc.): ");
        String bloodType = scanner.nextLine();

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection failed!");
            return;
        }

        // SQL query to search donors by blood type
        String sql = "SELECT * FROM donors WHERE blood_type = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bloodType);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No donors found with blood type: " + bloodType);
            } else {
                System.out.println("\nDonors Found:");
                while (rs.next()) {
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Blood Type: " + rs.getString("blood_type"));
                    System.out.println("Age: " + rs.getInt("age"));
                    System.out.println("Weight: " + rs.getDouble("weight"));
                    System.out.println("Medical Conditions: " + rs.getString("medical_conditions"));
                    System.out.println("---------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching donors: " + e.getMessage());
        }
    }
}

