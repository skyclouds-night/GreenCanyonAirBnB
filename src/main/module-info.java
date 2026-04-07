module ph.edu.dlsu.greencanyonairbnb {
        requires javafx.controls;
        requires javafx.fxml;

        opens ph.edu.dlsu.greencanyonairbnb to javafx.fxml;
        exports ph.edu.dlsu.greencanyonairbnb;
        }