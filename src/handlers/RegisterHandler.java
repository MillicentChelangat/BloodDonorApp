package handlers;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterHandler {
    public static boolean registerDonor(String username, String email, String password, String bloodType, int age, double weight, String medicalConditions) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection failed!");
            return false;
        }

        // Ensure donor meets basic eligibility
        if (age < 18) {
            System.out.println("Registration failed: Donors must be at least 18 years old.");
            return false;
        }
        if (weight < 50) {
            System.out.println("Registration failed: Donors must weigh at least 50 kg.");
            return false;
        }

        String sql = "INSERT INTO donors (username, email, password, blood_type, age, weight, medical_conditions) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, bloodType);
            stmt.setInt(5, age);
            stmt.setDouble(6, weight);
            stmt.setString(7, medicalConditions);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Donor registered successfully!");
                return true;
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Registration failed: Username or email already exists.");
            } else {
                System.out.println("Registration failed: " + e.getMessage());
            }
        }
        return false;
    }
}
