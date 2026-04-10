package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ph.edu.dlsu.greencanyonairbnb.model.UserSession;


import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Optional;

public class LoginViewController {

    private static final String URL = "jdbc:mysql://mysql-385ce9aa-dlsu-b948.j.aivencloud.com:13121/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_5sND-yAwDsrsP5yiXoI";

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    private void goToHelloView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/hello-view.fxml"));
        switchScene(event);
    }

    private void switchScene(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToSignup(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/SignupView.fxml"));
        switchScene(event);
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String query = "SELECT user_id, full_name, role FROM Users WHERE username = ? AND password_hash = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, usernameField.getText());
            pstmt.setString(2,passwordField.getText());

            ResultSet rs = rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("full_name");
                String role = rs.getString("role");
                UserSession.setUser(id,name,role);
                String accountRole = UserSession.getRole();

                if ("admin".equalsIgnoreCase(accountRole)) {
                    root = FXMLLoader.load(getClass().getResource("/fxml/HostView.fxml"));
                    switchScene(event);
                } else {
                    root = FXMLLoader.load(getClass().getResource("/fxml/hello-view.fxml"));
                    switchScene(event);
                }
            }

            else {
//                System.out.println("Invalid Email or Password"); //Change to popup !
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Credentials");
                alert.setContentText("The username or password you entered is incorrect. Please try again.");

                alert.showAndWait();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}