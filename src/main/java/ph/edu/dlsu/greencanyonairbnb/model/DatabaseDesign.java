package ph.edu.dlsu.greencanyonairbnb.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DatabaseDesign {

    private static final String URL = "jdbc:mysql://mysql-385ce9aa-dlsu-b948.j.aivencloud.com:13121/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_5sND-yAwDsrsP5yiXoI";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initialize() {
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
                                "full_name VARCHAR(100)," +
                                "username VARCHAR(100)," +
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
                                "property_name VARCHAR(150) UNIQUE," +
                                "description TEXT," +
                                "address VARCHAR(255)," +
                                "price_per_night DECIMAL(10,2)," +
                                "bedrooms INT," +
                                "bathrooms INT," +
                                "parking INT," +
                                "pets_allowed VARCHAR(50)," +
                                "property_type VARCHAR(50)," +
                                "image_url VARCHAR(255) DEFAULT 'airbnbpic.jpg'," +
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
                                "guest_count INT," +
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

                //Sample Admin Id
                stmt.executeUpdate(
                        "INSERT IGNORE INTO Users (full_name, username, email, password_hash, phone_number, role)" +
                        "VALUES ('Admin Name', 'admin','test@example.com', 'admin_pass','09123450021', 'admin')"
                );
                //Sample Properties
                stmt.executeUpdate(
                        "INSERT IGNORE INTO Properties (admin_id, property_name, description, address, price_per_night, bedrooms, bathrooms, parking, pets_allowed, property_type)" +
                                "VALUES (1, 'Test Airbnb', 'Somewhere me thinks', 'Taft Avenue, Manila', '5000', 2, 2, 3, 'YES', '2-Bedroom')"
                );

                stmt.executeUpdate(
                        "INSERT IGNORE INTO Properties (admin_id, property_name, description, address, price_per_night, bedrooms, bathrooms, parking, pets_allowed, property_type)" +
                                "VALUES (1, 'Condo Test', 'Also somewhere!', 'Makati, Manila', '6000', 2, 2, 3, 'YES', '1-Bedroom')"
                );

                stmt.executeUpdate(
                        "INSERT IGNORE INTO Properties (admin_id, property_name, description, address, price_per_night, bedrooms, bathrooms, parking, pets_allowed, property_type)" +
                                "VALUES (1, 'Studio thing', 'its cool', 'Quezon City', '4500', 2, 2, 3, 'YES', 'Studio')"
                );

                stmt.executeUpdate(
                        "INSERT IGNORE INTO Properties (admin_id, property_name, description, address, price_per_night, bedrooms, bathrooms, parking, pets_allowed, property_type)" +
                                "VALUES (1, 'Fancy Penthouse', 'kinda rich', 'BGC, Manila', '12000', 2, 2, 3, 'YES', 'Penthouse')"
                );

                System.out.println("Sample Admin and Property Created");

                System.out.println("CURRENT USERS:");
                try (ResultSet rsUsers = stmt.executeQuery("SELECT user_id, full_name, role, email FROM Users")) {
                    while (rsUsers.next()) {
                        System.out.printf("ID: %d | Name: %-15s | Role: %-6s | Email: %s%n",
                                rsUsers.getInt("user_id"),
                                rsUsers.getString("full_name"),
                                rsUsers.getString("role"),
                                rsUsers.getString("email"));
                    }
                }

                System.out.println("CURRENT PROPERTIES:");
                System.out.println("\n--- Current Properties in Database ---");
                try (ResultSet rsProps = stmt.executeQuery("SELECT property_id, property_name, price_per_night FROM Properties")) {
                    while (rsProps.next()) {
                        System.out.printf("ID: %d | Property: %-20s | Price: ₱%.2f%n",
                                rsProps.getInt("property_id"),
                                rsProps.getString("property_name"),
                                rsProps.getDouble("price_per_night"));
                    }
                }


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
