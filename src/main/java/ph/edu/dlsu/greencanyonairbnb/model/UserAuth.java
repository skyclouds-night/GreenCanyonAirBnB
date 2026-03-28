package ph.edu.dlsu.greencanyonairbnb.model;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UserAuth {
    private static final String URL = "jdbc:mysql://mysql-385ce9aa-dlsu-b948.j.aivencloud.com:13121/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_5sND-yAwDsrsP5yiXoI";

    public void register(String firstName, String lastName, String email, String password, String role) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO Users (first_name, last_name, email, password_hash, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, hashedPassword);
            pstmt.setString(5, role.toLowerCase());
            pstmt.executeUpdate();

            System.out.println("User registered successfully.");
        } catch (SQLException e) {
            System.err.println("Registration Error: " + e.getMessage());
        }
    }

    public boolean authenticate(String email, String password) {
        String query = "SELECT password_hash, role FROM Users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");

                    if (BCrypt.checkpw(password, storedHash)) {
                        String role = rs.getString("role");
                        System.out.println("User authenticated successfully as: " + role);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Auth Error: " + e.getMessage());
        }

        System.out.println("Login failed. Check email or password.");
        return false;
    }
}
