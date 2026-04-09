package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class EditPropertyViewController {

    private static final String URL = "jdbc:mysql://mysql-385ce9aa-dlsu-b948.j.aivencloud.com:13121/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_5sND-yAwDsrsP5yiXoI";

    @FXML
    private ToggleGroup place;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField bedroomField;

    @FXML
    private TextField bathroomField;

    @FXML
    private TextField parkingField;

    @FXML
    private TextField petsField;

    @FXML
    private TextField priceField;

    private Stage stage;
    private Scene scene;
    private Parent root;



    @FXML
    private void Save(ActionEvent event) throws IOException {
        String currentUser = "admin";
        int adminId = -1;

        int bedrooms = getIntOrZero(bedroomField.getText());
        int bathrooms = getIntOrZero(bathroomField.getText());
        int parking = getIntOrZero(parkingField.getText());
        double price = getDoubleOrZero(priceField.getText());

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            String getUserSql = "SELECT user_id FROM Users WHERE username = ?";
            try (PreparedStatement getUserPstmt = conn.prepareStatement(getUserSql)) {
                getUserPstmt.setString(1, currentUser);
                var rs = getUserPstmt.executeQuery();
                if (rs.next()) {
                    adminId = rs.getInt("user_id");
                }
            }

            if (adminId == -1) {
                System.err.println("Error: Admin user not found in database.");
                return;
            }

            String sql = "INSERT INTO Properties (admin_id, property_name, description, address, price_per_night, bedrooms, bathrooms, parking, pets_allowed) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                RadioButton selected = (RadioButton) place.getSelectedToggle();

                pstmt.setInt(1, adminId); // Using the ID we just fetched
                pstmt.setString(2, selected != null ? selected.getText() : "Apartment");
                pstmt.setString(3, descriptionArea.getText());
                pstmt.setString(4, "Taft Avenue, Manila");
                pstmt.setDouble(5, price);
                pstmt.setInt(6, bedrooms);
                pstmt.setInt(7, bathrooms);
                pstmt.setInt(8, parking);
                pstmt.setString(9, petsField.getText());

                pstmt.executeUpdate();
                System.out.println("Saved property for Admin ID: " + adminId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToHelloView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/HelloView.fxml"));
        switchScene(event);
    }

    @FXML
    private void goToListingsView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/ListingsView.fxml"));
        switchScene(event);
    }

    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));
        switchScene(event);
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/AdminAccountView.fxml"));
        switchScene(event);
    }

    private void switchScene(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private int getIntOrZero(String text) {
        try { return text.isEmpty() ? 0 : Integer.parseInt(text); }
        catch (NumberFormatException e) { return 0; }
    }

    private double getDoubleOrZero(String text) {
        try { return text.isEmpty() ? 0.0 : Double.parseDouble(text); }
        catch (NumberFormatException e) { return 0.0; }
    }

}
