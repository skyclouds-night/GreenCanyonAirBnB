package ph.edu.dlsu.greencanyonairbnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class GreenCanyonAirbnbApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenCanyonAirbnbApplication.class, args);
	}

}
