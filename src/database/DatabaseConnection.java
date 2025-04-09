package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/blood_donation?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Milly.23";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Ensure the MySQL driver is available
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Optional in modern versions, but safe
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Failed to close the database connection: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Test the connection
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null) {
                // Add any operations you want to perform on the connection here
            }
        } finally {
            // Ensure connection is closed in the finally block
            closeConnection(conn);
        }
    }
}
