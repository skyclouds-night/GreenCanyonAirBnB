package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ph.edu.dlsu.greencanyonairbnb.model.DatabaseDesign;
import ph.edu.dlsu.greencanyonairbnb.model.UserSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        refreshBookingLabels();
    }

    private void refreshBookingLabels() {
        int currentUserId = UserSession.getUserId();

        // SQL Query joining Bookings and Properties to get the Property Name
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

                // If you don't have a guest count in your DB yet, we use a placeholder
                Guests.setText("1 Guest");
            } else {
                // Default text if no booking is found
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

}