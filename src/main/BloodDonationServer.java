package main;

import handlers.AppointmentHandler;
import handlers.LoginHandler;
import handlers.NotificationHandler;
import handlers.SmsHandler;

import java.util.Scanner;

public class BloodDonationServer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int donorId = -1; // Stores logged-in donor ID

        while (true) {
            System.out.println("\n=== Blood Donation System ===");
            System.out.println("1. Register as a Donor");
            System.out.println("2. Login");
            System.out.println("3. Book an Appointment");
            System.out.println("4. View My Appointments");
            System.out.println("5. View Notifications");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) { // Prevents input errors
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Registration feature not implemented yet.");
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    donorId = LoginHandler.getDonorId(username, password);
                    if (donorId != -1) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                    break;

                case 3:
                    if (donorId == -1) {
                        System.out.println("You must be logged in to book an appointment.");
                    } else {
                        System.out.print("Enter donation center name: ");
                        String centerName = scanner.nextLine();
                        System.out.print("Enter appointment date (YYYY-MM-DD): ");
                        String appointmentDate = scanner.nextLine();

                        boolean success = AppointmentHandler.bookAppointment(donorId, centerName, appointmentDate);
                        if (success) {
                            System.out.println("Your appointment has been booked!");

                            // 🔹 Send an SMS after booking
                            String message = "Your appointment at " + centerName + " on " + appointmentDate + " has been confirmed!";
                            NotificationHandler.addNotification(donorId, message);

// Send SMS
                            String donorPhoneNumber = LoginHandler.getPhoneNumber(donorId); // Implement this method
                            SmsHandler.sendSms(donorPhoneNumber, message);

                        } else {
                            System.out.println("Failed to book appointment. Please try again.");
                        }
                    }
                    break;

                case 4:
                    if (donorId == -1) {
                        System.out.println("You must be logged in to view your appointments.");
                    } else {
                        AppointmentHandler.viewAppointments(donorId);
                    }
                    break;

                case 5:
                    if (donorId == -1) {
                        System.out.println("You must be logged in to view notifications.");
                    } else {
                        NotificationHandler.viewNotifications(donorId);
                    }
                    break;

                case 6:
                    System.out.println("Exiting... Thank you for using the Blood Donation System.");
                    scanner.close();
                    return; // Exit program

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
