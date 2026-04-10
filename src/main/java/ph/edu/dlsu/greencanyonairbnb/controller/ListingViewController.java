package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.UserSession;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;


public class ListingViewController implements Initializable {

    private static final String URL = "jdbc:mysql://mysql-385ce9aa-dlsu-b948.j.aivencloud.com:13121/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_5sND-yAwDsrsP5yiXoI";

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private FlowPane propertyFlowPane;

    private ObservableList<Property> propertyList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        propertyFlowPane.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        propertyFlowPane.getChildren().clear();
        loadAllProperties();

    }

    @SneakyThrows
    private void loadAllProperties() {
        propertyList.clear();
        String query = "SELECT property_id, property_name, price_per_night, address, image_url FROM Properties";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Property p = new Property(
                        rs.getInt("property_id"),
                        rs.getString("property_name"),
                        rs.getDouble("price_per_night"),
                        rs.getString("address"),
                        rs.getString("image_url")
                );
                String imgUrl = p.getImageUrl();
                if (imgUrl == null) {
                    imgUrl = "airbnbpic.jpg";
                }

                propertyList.add(p);
                System.out.println("Property Added: "+ p.getPropertyName());

                String imagePath = getClass().getResource("/img/" + imgUrl).toExternalForm();
                Image propertyImage = new Image(imagePath);

                ImageView imageView = new ImageView(propertyImage);
                imageView.setFitWidth(250);
                imageView.setFitHeight(120);
                imageView.setPreserveRatio(true);

                Label propertyPrice =  new Label("₱" + String.valueOf(p.getPrice()));
                Button propertyName = new Button(p.getPropertyName());

                propertyName.setOnAction(event -> {
                    try {
                        goToPropertyDetails(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                VBox propertyCard = new VBox(12);
                propertyCard.getStyleClass().add("vbox-listings");
                propertyCard.setPrefWidth(250);
                propertyCard.setPrefHeight(270);
                propertyCard.setMaxWidth(250);
                propertyCard.setMaxHeight(270);
                propertyCard.setMinWidth(250);
                propertyCard.setMinHeight(270);
                propertyCard.setMaxWidth(250);
                propertyCard.setMaxHeight(270);
                propertyCard.setFillWidth(true);
                propertyCard.setCacheShape(true);
                propertyCard.setCenterShape(true);
                propertyCard.setScaleShape(true);

                propertyCard.setMargin(imageView, new Insets(10, 0, 0, 35));
                propertyCard.setMargin(propertyPrice, new Insets(10, 0, 0, 15));

                propertyName.getStyleClass().add("property-name-button");
                propertyPrice.getStyleClass().add("property-price");
                imageView.getStyleClass().add("img");

                propertyCard.getChildren().addAll(imageView, propertyName, propertyPrice);
                propertyFlowPane.getChildren().add(propertyCard);

            }


        } catch (SQLException e) {
            e.printStackTrace();
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