package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloViewController {

        public Button listingsView2;
        public Button listingsView3;
        public Button listingsView4;
        public Button listingsView5;
        private Stage stage;
        private Scene scene;
        private Parent root;

        @FXML
        private void goToHelloView(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("/ph/edu/dlsu/automatedairbnbbookingsystem/hello-view.fxml"));
            switchScene(event);
        }

        @FXML
        private void goToListingsView(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("/ph/edu/dlsu/automatedairbnbbookingsystem/ListingsView.fxml"));
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
            root = FXMLLoader.load(getClass().getResource("/ph/edu/dlsu/automatedairbnbbookingsystem/LoginView.fxml"));
            switchScene(event);
        }
}
