package ph.edu.dlsu.greencanyonairbnb;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;

import static javafx.application.Application.launch;
import static org.apache.tomcat.util.file.ConfigFileLoader.getSource;

@SpringBootApplication
public class GreenCanyonAirBnBApplication extends Application {

	private ConfigurableApplicationContext context;
	private Scene scene;

	@Override
	public void init() throws Exception {
		ApplicationContextInitializer<GenericApplicationContext> initializer = ac -> {
			ac.registerBean(Application.class, () -> GreenCanyonAirBnBApplication.this);
			ac.registerBean(Parameters.class, () -> getParameters());
			ac.registerBean(HostServices.class, () -> getHostServices());
		};
		this.context = new SpringApplicationBuilder()
				.sources(AppLauncher.class)
				.initializers()
				.web(org.springframework.boot.WebApplicationType.NONE)
				.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.context.publishEvent(new StageReadyEvent(stage));
		stage.setTitle("Automated AirBnB Booking System");
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/hello-view.fxml"));
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root, 1000, 800);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		this.context.close();
		Platform.exit();
	}

	class StageReadyEvent extends ApplicationEvent {
		public Stage getStage() {
			return Stage.class.cast(getSource());
		}

		public StageReadyEvent(Stage source) {
			super(source);
		}
	}


}


