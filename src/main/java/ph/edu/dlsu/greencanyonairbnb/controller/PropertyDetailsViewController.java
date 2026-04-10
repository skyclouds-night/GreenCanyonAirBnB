package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.UserSession;

import java.io.IOException;

public class PropertyDetailsViewController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label propertyName;
    @FXML
    private Label propertyAddress;
    @FXML
    private Label propertyDescription;
    @FXML
    private Label propertyPricePerNight;
    @FXML
    private ImageView propertyImage;
    @FXML
    private ImageView propertyImage1;

    private Property selectedProperty;

    public void setPropertyData(Property property) {
        this.selectedProperty = property;

        propertyName.setText(property.getPropertyName());
        propertyPricePerNight.setText("₱" + property.getPrice() + "/Night");
        propertyAddress.setText(property.getPropertyAddress());
        propertyDescription.setText(property.getPropertyAddress());
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
        root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));
        switchScene(event);
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/ReservationView.fxml"));
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
