package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ph.edu.dlsu.greencanyonairbnb.service.PropertyService;
import ph.edu.dlsu.greencanyonairbnb.model.Property;

import java.io.IOException;
import java.util.List;

@Controller
public class HelloViewController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping("/")
    public String home(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);
        return "index";//index.html
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void goToHelloView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/ph/edu/dlsu/greencanyonairbnb/hello-view.fxml"));
        switchScene(event);
    }

    @FXML
    private void goToListingsView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/ph/edu/dlsu/greencanyonairbnb/ListingsView.fxml"));
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
        root = FXMLLoader.load(getClass().getResource("/ph/edu/dlsu/greencanyonairbnb/LoginView.fxml"));
        switchScene(event);
    }
}