package handlers;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SmsHandler {

    private static final String API_USERNAME = "your_username";
    private static final String API_KEY = "your_api_key";
    private static final String SMS_URL = "https://api.africastalking.com/version1/messaging";

    public static void sendSms(String phoneNumber, String message) {
        try {

            String postData = "username=" + URLEncoder.encode(API_USERNAME, StandardCharsets.UTF_8)
                    + "&to=" + URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8)
                    + "&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);


            URL url = new URL(SMS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("apiKey", API_KEY);
            conn.setDoOutput(true);


            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes());
            }


            int responseCode = conn.getResponseCode();
            if (responseCode == 201) {
                System.out.println("SMS sent successfully to " + phoneNumber);
            } else {
                System.out.println("Failed to send SMS. Response Code: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        sendSms("+254725995840", "Urgent: We need O+ blood at Nairobi CBD Blood Bank. Please visit us if you can donate.");
    }
}

