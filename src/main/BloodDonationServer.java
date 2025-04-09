package main;

import handlers.LoginHandler;
import handlers.RegisterHandler;
import handlers.MainMenuHandler;
import java.util.Scanner;

public class BloodDonationServer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        String loggedInUser = null;  // Variable to store the logged-in username

        while (running) {
            // If the user is logged in, show the menu with their username
            if (loggedInUser != null) {
                System.out.println("\nWelcome, " + loggedInUser + "!");
                System.out.println("1. Become a Donor");
                System.out.println("2. Search for Donors");
                System.out.println("3. Book an Appointment");
                System.out.println("4. Logout");
            } else {
                System.out.println("\n--- Welcome to the Blood Donation Management System ---");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
            }

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            switch (choice) {
                case 1:
                    if (loggedInUser == null) {
                        // Register User
                        System.out.print("Enter username: ");
                        String regUsername = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String regEmail = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String regPassword = scanner.nextLine();

                        boolean registered = RegisterHandler.registerUser(regUsername, regEmail, regPassword);
                        if (registered) {
                            System.out.println("Registration successful. You can now log in.");
                        } else {
                            System.out.println("Registration failed. Please try again.");
                        }
                    } else {
                        // Become a Donor (This will only work if the user is logged in)
                        System.out.println("You are already logged in as " + loggedInUser + ". You can proceed to become a donor.");
                        // Add donor registration logic here...
                    }
                    break;

                case 2:
                    if (loggedInUser == null) {
                        // Login User
                        System.out.print("Enter username: ");
                        String loginUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String loginPassword = scanner.nextLine();

                        boolean loggedIn = LoginHandler.login(loginUsername, loginPassword);
                        if (loggedIn) {
                            loggedInUser = loginUsername;  // Store the logged-in username
                            System.out.println("Login successful!");
                        } else {
                            System.out.println("Login failed. Invalid credentials.");
                        }
                    } else {
                        // Search for Donors logic goes here...
                    }
                    break;

                case 3:
                    if (loggedInUser == null) {
                        // Exit the system
                        System.out.println("Exiting the system. Goodbye!");
                        running = false;
                    } else {
                        // Book Appointment logic goes here...
                        System.out.println("Booking an appointment...");
                    }
                    break;

                case 4:
                    if (loggedInUser != null) {
                        System.out.println("Logging out " + loggedInUser + "...");
                        loggedInUser = null;  // Clear the logged-in username
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }

            // If the user is logged in, pass the username to the MainMenuHandler
            if (loggedInUser != null) {
                MainMenuHandler.showMainMenu(loggedInUser);
            }
        }
        scanner.close();
    }
}
