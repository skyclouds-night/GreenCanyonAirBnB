package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller

public class SignupViewController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField passwordField;
    @FXML
    private RadioButton guestButton;
    @FXML
    private RadioButton adminButton;
    @FXML
    private ToggleGroup userTypeGroup;


    private static final String URL = "jdbc:mysql://mysql-385ce9aa-dlsu-b948.j.aivencloud.com:13121/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASS = "AVNS_5sND-yAwDsrsP5yiXoI";


    private Stage stage;
    private Scene scene;
    private Parent root;


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
    private void goToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));
        switchScene(event);
    }

    @FXML
    private void signUpAccount(ActionEvent event) throws IOException {
        String sql = "INSERT IGNORE INTO Users (full_name, user_name, email, password_hash, phone_number, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        RadioButton selectedRole =  (RadioButton) userTypeGroup.getSelectedToggle();
        String roleText = "";
        if (selectedRole != null) {
            roleText = selectedRole.getText().toLowerCase();
            System.out.println("Role Selected: " +roleText);
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fullNameField.getText());
            pstmt.setString(2, usernameField.getText());
            pstmt.setString(3, emailField.getText());
            pstmt.setString(4, passwordField.getText());
            pstmt.setString(5, phoneNumberField.getText());
            pstmt.setString(6, roleText);

            pstmt.executeUpdate();

            System.out.println("Account Created Successfully!");
            root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));
            switchScene(event);

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
    }
}