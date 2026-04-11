package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ph.edu.dlsu.greencanyonairbnb.model.DatabaseDesign;
import ph.edu.dlsu.greencanyonairbnb.model.UserSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class GuestAccountViewController {

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
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));
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
        refreshBookingLabels();
    }

    private void refreshBookingLabels() {
        int currentUserId = UserSession.getUserId();

        String sql = "SELECT p.property_name, b.check_in_date, b.total_price " +
                "FROM Bookings b " +
                "JOIN Properties p ON b.property_id = p.property_id " +
                "WHERE b.guest_id = ? " +
                "ORDER BY b.created_at DESC LIMIT 1";

        try (Connection conn = DatabaseDesign.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                property.setText(rs.getString("property_name"));
                CheckIn.setText(rs.getString("check_in_date"));
                totalPrice.setText(String.format("₱%.2f", rs.getDouble("total_price")));

                Guests.setText("1 Guest");
            } else {
                property.setText("No Bookings Found");
                CheckIn.setText("--");
                totalPrice.setText("₱0.00");
                Guests.setText("--");
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelReservation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Reservation");
        alert.setHeaderText("Are you sure you want to cancel your booking?");
        alert.setContentText("This will notify the Admin and permanently remove your reservation.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            processCancellationAndNotify();
        }
    }

    private void processCancellationAndNotify() {
        int userId = UserSession.getUserId();
        String userName = UserSession.getUsername(); // Ensure this is set during login

        // SQL Queries
        String notifySql = "INSERT INTO Messages (sender_id, receiver_id, message_text, read_status) VALUES (?, ?, ?, ?)";
        String deleteSql = "DELETE FROM Bookings WHERE guest_id = ? ORDER BY created_at DESC LIMIT 1";

        try (Connection conn = DatabaseDesign.getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement pstmtNotify = conn.prepareStatement(notifySql)) {
                    pstmtNotify.setInt(1, userId); // Sender
                    pstmtNotify.setInt(2, 1);      // Receiver (Admin)
                    pstmtNotify.setString(3, "ALERT: Guest " + userName + " has cancelled their most recent booking.");
                    pstmtNotify.setBoolean(4, false); // Unread
                    pstmtNotify.executeUpdate();
                }

                try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {
                    pstmtDelete.setInt(1, userId);
                    int rowsAffected = pstmtDelete.executeUpdate();

                    if (rowsAffected > 0) {
                        conn.commit();

                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Cancelled");
                        success.setHeaderText(null);
                        success.setContentText("Your reservation has been removed and the Admin has been notified.");
                        success.showAndWait();

                        property.setText("No Bookings Found");
                        CheckIn.setText("--");
                        totalPrice.setText("₱0.00");
                        Guests.setText("--");
                    } else {
                        conn.rollback();
                        System.err.println("No booking found to delete.");
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Error during cancellation process: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

