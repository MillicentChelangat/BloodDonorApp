package handlers;

public class NotificationHandler {

    public static void sendNotification(int donorId, String message) {
        System.out.println("Sending notification to Donor ID " + donorId + ": " + message);
    }
}
