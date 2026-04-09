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

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class EditPropertyViewController {

    private static final String URL = "jdbc:mysql://mysql-385ce9aa-dlsu-b948.j.aivencloud.com:13121/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_5sND-yAwDsrsP5yiXoI";

    @FXML private ToggleGroup place;
    @FXML private TextArea descriptionArea;
    @FXML private TextField bedroomField;
    @FXML private TextField bathroomField;
    @FXML private TextField parkingField;
    @FXML private TextField petsField;

    private Stage stage;
    private Scene scene;
    private Parent root;



    @FXML
    private void Save(ActionEvent event) throws IOException {
// 1. Extract data from UI
        RadioButton selectedRadioButton = (RadioButton) place.getSelectedToggle();
        String propertyType = (selectedRadioButton != null) ? selectedRadioButton.getText() : "Not Specified";
        String description = descriptionArea.getText();

        // Handle potential empty strings for numeric fields to avoid NumberFormatExceptions
        int bedrooms = bedroomField.getText().isEmpty() ? 0 : Integer.parseInt(bedroomField.getText());
        int bathrooms = bathroomField.getText().isEmpty() ? 0 : Integer.parseInt(bathroomField.getText());
        int parking = parkingField.getText().isEmpty() ? 0 : Integer.parseInt(parkingField.getText());
        String pets = petsField.getText();

        // 2. Database Logic
        String sql = "INSERT INTO Properties (admin_id, property_name, description, address, price_per_night, bedrooms, bathrooms, parking, pets_allowed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, 1); // Mock Admin ID
            pstmt.setString(2, propertyType);
            pstmt.setString(3, description);
            pstmt.setString(4, "Taft Avenue, Manila");
            pstmt.setDouble(5, 0.0);
            pstmt.setInt(6, bedrooms);
            pstmt.setInt(7, bathrooms);
            pstmt.setInt(8, parking);
            pstmt.setString(9, pets);

            pstmt.executeUpdate();
            System.out.println("All property details saved successfully!");

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Format Error: Please enter numbers for Bedrooms/Bathrooms/Parking.");
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

}
