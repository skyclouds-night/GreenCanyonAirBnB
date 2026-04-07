package ph.edu.dlsu.greencanyonairbnb.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseDesign {

    private static final String URL = "jdbc:mysql://mysql-385ce9aa-dlsu-b948.j.aivencloud.com:13121/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_5sND-yAwDsrsP5yiXoI";

    public static void main(String[] args) {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to Aiven DB
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                // USERS TABLE
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS Users (" +
                                "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                                "first_name VARCHAR(100)," +
                                "last_name VARCHAR(100)," +
                                "email VARCHAR(150) UNIQUE NOT NULL," +
                                "password_hash VARCHAR(255) NOT NULL," +
                                "phone_number VARCHAR(20)," +
                                "role ENUM('guest','admin') NOT NULL," +
                                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
                );
                // PROPERTIES TABLE
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS Properties (" +
                                "property_id INT AUTO_INCREMENT PRIMARY KEY," +
                                "admin_id INT," +
                                "property_name VARCHAR(150)," +
                                "description TEXT," +
                                "address VARCHAR(255)," +
                                "price_per_night DECIMAL(10,2)," +
                                "max_guests INT," +
                                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                                "FOREIGN KEY (admin_id) REFERENCES Users(user_id))"
                );

                // PROPERTY PHOTOS
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS Property_Photos (" +
                                "photo_id INT AUTO_INCREMENT PRIMARY KEY," +
                                "property_id INT," +
                                "photo_url VARCHAR(255)," +
                                "uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                                "FOREIGN KEY (property_id) REFERENCES Properties(property_id))"
                );

                // BOOKINGS
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS Bookings (" +
                                "booking_id INT AUTO_INCREMENT PRIMARY KEY," +
                                "property_id INT," +
                                "guest_id INT," +
                                "check_in_date DATE," +
                                "check_out_date DATE," +
                                "total_price DECIMAL(10,2)," +
                                "booking_status ENUM('pending','confirmed','cancelled')," +
                                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                                "FOREIGN KEY (property_id) REFERENCES Properties(property_id)," +
                                "FOREIGN KEY (guest_id) REFERENCES Users(user_id))"
                );

                // PAYMENTS
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS Payments (" +
                                "payment_id INT AUTO_INCREMENT PRIMARY KEY," +
                                "booking_id INT," +
                                "amount DECIMAL(10,2)," +
                                "payment_method VARCHAR(50)," +
                                "payment_status ENUM('pending','completed','failed')," +
                                "transaction_reference VARCHAR(255)," +
                                "payment_date DATETIME," +
                                "FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id))"
                );

                // REVIEWS
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS Reviews (" +
                                "review_id INT AUTO_INCREMENT PRIMARY KEY," +
                                "property_id INT," +
                                "guest_id INT," +
                                "rating INT," +
                                "comment TEXT," +
                                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                                "FOREIGN KEY (property_id) REFERENCES Properties(property_id)," +
                                "FOREIGN KEY (guest_id) REFERENCES Users(user_id))"
                );

                // MESSAGES
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS Messages (" +
                                "message_id INT AUTO_INCREMENT PRIMARY KEY," +
                                "sender_id INT," +
                                "receiver_id INT," +
                                "message_text TEXT," +
                                "sent_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                                "read_status BOOLEAN DEFAULT FALSE," +
                                "FOREIGN KEY (sender_id) REFERENCES Users(user_id)," +
                                "FOREIGN KEY (receiver_id) REFERENCES Users(user_id))"
                );

                // SYSTEM LOGS
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS System_Logs (" +
                                "log_id INT AUTO_INCREMENT PRIMARY KEY," +
                                "user_id INT," +
                                "action VARCHAR(255)," +
                                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                                "ip_address VARCHAR(50)," +
                                "FOREIGN KEY (user_id) REFERENCES Users(user_id))"
                );

                // Payment Methods
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS payment_accounts (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "admin_id INT NOT NULL," +
                        "method ENUM('GCash','Maya','QRPh') NOT NULL," + //GCash, Maya, QRPh
                        "account_name VARCHAR(100)," +
                        "account_number VARCHAR(50)," +
                        "qr_image LONGBLOB,"  +
                        "is_active BOOLEAN DEFAULT TRUE,"  +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)" );

                System.out.println("Database tables created successfully.");

            }

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
