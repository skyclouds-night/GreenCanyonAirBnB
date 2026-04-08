package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ReservationViewController {

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
    private void goToReservation(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/ReservationView.fxml"));
        switchScene(event);
    }

//    @FXML
//    private DatePicker datePicker;
//
//    @FXML
//    private Label selectedDate;
//
//    @FXML
//    void datePicker(ActionEvent evt) {
//        LocalDate localDate = datePicker.getValue();
//        String pattern = "MMMM dd, yyyy";
//        String datePattern = localDate.format(DateTimeFormatter.ofPattern(""));
//        selectedDate.setText("Selected Date: " +datePicker.getValue().toString());
//    }

}