package ph.edu.dlsu.greencanyonairbnb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ph.edu.dlsu.greencanyonairbnb.model.DatabaseDesign;

import java.io.IOException;


public class GreenCanyonAirBnBApplication extends Application {


    @Override
    public void start (Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/hello-view.fxml"));

        stage.setScene(new Scene(root,1000,800));
        stage.show();


    }

    public static void main(String[] args) {
        DatabaseDesign.initialize();

        launch(args);
    }

}