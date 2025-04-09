package handlers;

import java.util.Scanner;

public class MainMenuHandler {

    private static String userName = "";
    private static boolean isDonor = false;

    public static void showMainMenu(String loggedInUser) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWelcome, " + loggedInUser + "!");
        System.out.println("1. Become a Donor");
        System.out.println("2. Search for Donors");
        System.out.println("3. Book an Appointment");
        System.out.println("4. Logout");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                becomeDonor(); // Call the method to register as a donor
                break;
            case 2:
                System.out.println("Searching for Donors...");
                break;
            case 3:
                System.out.println("Booking an Appointment...");
                break;
            case 4:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice! Try again.");
                showMainMenu(loggedInUser);
                break;
        }
    }

    private static void becomeDonor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Donor Registration ---");
        System.out.print("Enter your full name: ");
        String fullName = scanner.nextLine();

        System.out.print("Enter your age: ");
        int age = scanner.nextInt();

        System.out.print("Enter your weight (in kg): ");
        double weight = scanner.nextDouble();

        System.out.print("Enter your gender (Male/Female/Other): ");
        scanner.nextLine();
        String gender = scanner.nextLine();

        System.out.print("Enter your blood type (e.g., A+, O-, etc.): ");
        String bloodType = scanner.nextLine();

        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Are you medically eligible to donate? (yes/no): ");
        String medicalEligibility = scanner.nextLine();

        boolean registrationSuccess = DonorRegistrationHandler.registerDonor();  // Correct method call

        if (registrationSuccess) {
            isDonor = true;
            userName = fullName;

            System.out.println("You are now a registered donor!");
            showMainMenu(userName);
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }
}
