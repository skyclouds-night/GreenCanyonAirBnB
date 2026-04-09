package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ph.edu.dlsu.greencanyonairbnb.model.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelloViewController implements Initializable {
    @FXML
    private Button loginBtn;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String currentRole = UserSession.getRole();
    private String currentUsername = UserSession.getUsername();

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        if (currentUsername != null && currentRole != null) {
            loginBtn.setText("Log Out");
        } else {
            loginBtn.setText("Login");
        }

    }


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
        if (currentUsername != null && currentRole != null) {
            UserSession.endSession();
        }
        root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));
        switchScene(event);

    }

    @FXML
    private void goToPropertyDetails(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/PropertyDetails.fxml"));
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

}