package handlers;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DonorRegistrationHandler {

    private static Scanner scanner = new Scanner(System.in);

    public static boolean registerDonor() {
        System.out.println("\n--- Donor Registration ---");
        System.out.print("Enter your full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your blood type (e.g., A+, O-, etc.): ");
        String bloodType = scanner.nextLine();

        System.out.print("Enter your age: ");
        int age = scanner.nextInt();

        System.out.print("Enter your weight (in kg): ");
        double weight = scanner.nextDouble();

        System.out.print("Enter your gender (Male/Female/Other): ");
        scanner.nextLine();
        String gender = scanner.nextLine();

        System.out.print("Do you have any medical conditions? (yes/no): ");
        String medicalConditions = scanner.nextLine();

        // Pre-screening check
        if (!preScreening()) {
            System.out.println("You are not eligible to donate blood based on the pre-screening results.");
            return false;  // Return false if pre-screening fails
        }

        // Check eligibility after pre-screening
        if (!checkEligibility(age, weight, medicalConditions)) {
            System.out.println("You are not eligible to donate blood based on the provided details.");
            return false;  // Return false if eligibility check fails
        }

        // If eligible, proceed with registration
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection failed!");
            return false;  // Return false if connection fails
        }

        String sql = "INSERT INTO donors (name, email, blood_type, age, weight, gender, medical_conditions) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, bloodType);
            stmt.setInt(4, age);
            stmt.setDouble(5, weight);
            stmt.setString(6, gender);
            stmt.setString(7, medicalConditions);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Donor registration successful!");
                return true;
            } else {
                System.out.println("Registration failed. Please try again.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error registering donor: " + e.getMessage());
            return false;
        }
    }
    private static boolean preScreening() {
        System.out.println("\n--- Pre-Screening Questionnaire ---");
        System.out.print("Have you traveled to any malaria-endemic areas in the last 6 months? (yes/no): ");
        String travelHistory = scanner.nextLine();

        if (travelHistory.equalsIgnoreCase("yes")) {
            System.out.println("Eligibility: You are not eligible to donate blood if you have recently traveled to malaria-endemic areas.");
            return false;
        }

        System.out.print("Are you currently taking any medication? (yes/no): ");
        String onMedication = scanner.nextLine();

        if (onMedication.equalsIgnoreCase("yes")) {
            System.out.println("Eligibility: You cannot donate blood if you are taking certain medications. Please consult a doctor.");
            return false;
        }

        System.out.print("Do you drink alcohol regularly? (yes/no): ");
        String alcoholUse = scanner.nextLine();

        if (alcoholUse.equalsIgnoreCase("yes")) {
            System.out.println("Eligibility: You cannot donate blood if you consume alcohol regularly.");
            return false;
        }

        System.out.print("Do you smoke? (yes/no): ");
        String smoking = scanner.nextLine();

        if (smoking.equalsIgnoreCase("yes")) {
            System.out.println("Eligibility: You cannot donate blood if you are a regular smoker.");
            return false;
        }
        return true;
    }
    private static boolean checkEligibility(int age, double weight, String medicalConditions) {
        if (age < 18 || age > 65) {
            System.out.println("Eligibility: You must be between 18 and 65 years old.");
            return false;
        }
        if (weight < 50) {
            System.out.println("Eligibility: You must weigh at least 50 kg.");
            return false;
        }
        if (medicalConditions.equalsIgnoreCase("yes")) {
            System.out.println("Eligibility: You cannot donate blood if you have medical conditions.");
            return false;
        }

        return true;
    }
}
