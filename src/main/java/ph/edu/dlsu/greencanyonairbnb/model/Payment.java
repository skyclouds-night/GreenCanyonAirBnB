package ph.edu.dlsu.greencanyonairbnb.model;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Payment {

    private static final String URL = "jdbc:mysql://mysql-385ce9aa-dlsu-b948.j.aivencloud.com:13121/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_5sND-yAwDsrsP5yiXoI";

    public void addMethod(int adminId, String account_name, String account_number, String method, byte[] img) {

        String sql = "INSERT INTO payment_accounts(admin_id, method, account_name, account_number, qr_image) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setString(2, method);
            ps.setString(3, account_name);
            ps.setString(4, account_number);
            ps.setBytes(5, img);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PaymentAccount> getMethodsByAdmin(int adminId) {

        List<PaymentAccount> list = new ArrayList<>();

        String sql = "SELECT * FROM payment_accounts WHERE is_active = TRUE AND admin_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PaymentAccount(
                        rs.getInt("id"),
                        rs.getInt("admin_id"),
                        rs.getString("method"),
                        rs.getString("account_name"),
                        rs.getString("account_number"),
                        rs.getBytes("qr_image")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<PaymentAccount> getMethodsByProperty(int propertyId) {

        List<PaymentAccount> list = new ArrayList<>();

        String sql =
        "SELECT pa.*" +
        "FROM payment_accounts pa" +
        "JOIN Properties p ON pa.admin_id = p.admin_id" +
        "WHERE p.property_id = ? AND pa.is_active = TRUE";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, propertyId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PaymentAccount(
                        rs.getInt("id"),
                        rs.getInt("admin_id"),
                        rs.getString("method"),
                        rs.getString("account_name"),
                        rs.getString("account_number"),
                        rs.getBytes("qr_image")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public byte[] convertImageToByteArray(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Error: Could not read file at " + filePath);
            e.printStackTrace();
            return null;
        }
    }
}