package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ph.edu.dlsu.greencanyonairbnb.model.DatabaseDesign;
import ph.edu.dlsu.greencanyonairbnb.model.UserSession;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class AdminAccountViewController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label property;

    @FXML
    private Label CheckIn;

    @FXML
    private Label Guests;

    @FXML
    private Label totalPrice;


    @FXML
    private void goToHelloView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/hello-view.fxml"));
        switchScene(event);
    }

    @FXML
    private void goToListingsView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/ListingsView.fxml"));
        switchScene(event);
    }

    private void switchScene(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToAdminView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/HostView.fxml"));
        switchScene(event);
    }

    @FXML
    private void goToAccount(ActionEvent event) throws IOException {
        String accountRole = UserSession.getRole();
        System.out.println("Current Role: " + accountRole);

        if ("admin".equalsIgnoreCase(accountRole)) {
            root = FXMLLoader.load(getClass().getResource("/fxml/AdminAccountView.fxml"));
            switchScene(event);
        } else {
            root = FXMLLoader.load(getClass().getResource("/fxml/GuestAccountView.fxml"));
            switchScene(event);
        }

    }

    @FXML
    public void initialize() {
        loadPendingBookings();
    }

    public void loadPendingBookings() {
        String sql = "SELECT b.booking_id, b.guest_id, u.full_name, b.total_price " +
                "FROM Bookings b " +
                "JOIN Users u ON b.guest_id = u.user_id " +
                "WHERE b.booking_status = 'pending'";

        try (Connection conn = DatabaseDesign.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int bId = rs.getInt("booking_id");
                int gId = rs.getInt("guest_id");
                String name = rs.getString("full_name");

                System.out.println("Pending: " + name + " (Booking #" + bId + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAcceptBooking(ActionEvent event) {
        int bookingId = 1;
        int guestId = 2;

        String updateSql = "UPDATE Bookings SET booking_status = 'confirmed' WHERE booking_id = ?";
        String notifySql = "INSERT INTO Messages (sender_id, receiver_id, message_text, read_status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseDesign.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, bookingId);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmtNotify = conn.prepareStatement(notifySql)) {
                pstmtNotify.setInt(1, UserSession.getUserId());
                pstmtNotify.setInt(2, guestId);
                pstmtNotify.setString(3, "Your reservation #" + bookingId + " has been ACCEPTED.");
                pstmtNotify.setBoolean(4, false);
                pstmtNotify.executeUpdate();
            }

            conn.commit();
            showAlert("Success", "Booking confirmed and guest notified.");

        } catch (SQLException e) {
            showAlert("Error", "Try Again.");
        }
    }

    @FXML
    private void handleRejectBooking(ActionEvent event) {
        int bookingId = 1;
        int guestId = 2;

        String deleteSql = "DELETE FROM Bookings WHERE booking_id = ?";
        String notifySql = "INSERT INTO Messages (sender_id, receiver_id, message_text, read_status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseDesign.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtNotify = conn.prepareStatement(notifySql)) {
                pstmtNotify.setInt(1, UserSession.getUserId());
                pstmtNotify.setInt(2, guestId);
                pstmtNotify.setString(3, "Reservation #" + bookingId + " was REJECTED. Please check for other dates.");
                pstmtNotify.setBoolean(4, false);
                pstmtNotify.executeUpdate();
            }

            try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {
                pstmtDelete.setInt(1, bookingId);
                pstmtDelete.executeUpdate();
            }

            conn.commit();
            showAlert("Rejected", "Booking removed from database and guest notified.");

        } catch (SQLException e) {
            showAlert("Error", "Try Again.");
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}