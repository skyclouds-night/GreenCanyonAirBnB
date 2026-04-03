package ph.edu.dlsu.greencanyonairbnb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static javafx.application.Application.launch;

@SpringBootApplication
public class GreenCanyonAirBnBApplication extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/fxml/hello-view.fxml")
		);

		Scene scene = new Scene(fxmlLoader.load(), 1000, 800);

		scene.getStylesheets().add(
				getClass().getResource("/css/styles.css").toExternalForm()
		);

		stage.setTitle("Automated AirBnB Booking System");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}


