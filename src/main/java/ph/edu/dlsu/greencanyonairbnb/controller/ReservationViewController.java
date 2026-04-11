package ph.edu.dlsu.greencanyonairbnb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ph.edu.dlsu.greencanyonairbnb.model.DatabaseDesign;
import ph.edu.dlsu.greencanyonairbnb.model.Payment;
import ph.edu.dlsu.greencanyonairbnb.model.PaymentAccount;
import ph.edu.dlsu.greencanyonairbnb.model.UserSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.scene.image.ImageView;



public class ReservationViewController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private DatePicker datePicker;

    @FXML
    private DatePicker datePicker1;

    @FXML
    private Label selectedDate;

    @FXML
    private Label selectedDate1;

    @FXML
    private TextField num_guest;

    public void initialize() {
        loadPropertyPrice(1); // Manually trigger it for testing
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
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    void datePicker(ActionEvent evt) {
        LocalDate localDate = datePicker.getValue();
        String pattern = "MMMM dd, yyyy";
        String datePattern = localDate.format(DateTimeFormatter.ofPattern(pattern));
        selectedDate.setText("Selected Date: " + datePattern);

        calculateTotal(); //
    }

    @FXML
    void datePicker1(ActionEvent evt) {
        LocalDate localDate = datePicker1.getValue();
        String pattern = "MMMM dd, yyyy";
        String datePattern = localDate.format(DateTimeFormatter.ofPattern(pattern));
        selectedDate1.setText("Selected Date: " + datePattern);

        calculateTotal(); // Update total price
    }


    @FXML
    private ImageView PaymentQR;

    private Payment paymentModel = new Payment();

    public void loadAdminPaymentImage(int propertyID) {
        List<PaymentAccount> accounts = paymentModel.getMethodsByProperty(propertyID);

        if (!accounts.isEmpty()) {
            byte[] imageBytes = accounts.get(0).getQrImage();

            if (imageBytes != null) {
                Image img = new Image(new ByteArrayInputStream(imageBytes));

                PaymentQR.setImage(img);
            }
        }
    }

    @FXML
    private Label totalPriceLabel;

    private double pricePerNight = 0;

    public void loadPropertyPrice(int propertyId) {
        String sql = "SELECT price_per_night FROM Properties WHERE property_id = ?";

        try (Connection conn = DatabaseDesign.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.pricePerNight = rs.getDouble("price_per_night");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void calculateTotal() {
        LocalDate checkIn = datePicker.getValue();
        LocalDate checkOut = datePicker1.getValue();

        if (checkIn != null && checkOut != null && pricePerNight > 0) {

            if (checkOut.isAfter(checkIn)) {
                long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
                double total = nights * pricePerNight;
                totalPriceLabel.setText(String.format("Total for %d night(s): ₱%.2f", nights, total));
            } else {
                totalPriceLabel.setText("Check-out must be after check-in.");
            }
        } else if (pricePerNight == 0) {
            totalPriceLabel.setText("Loading price...");
        }
    }

    @FXML
    private void handleConfirmReservation(ActionEvent event) {
        LocalDate checkIn = datePicker.getValue();
        LocalDate checkOut = datePicker1.getValue();

        int numGuests = 0;
        try {
            numGuests = Integer.parseInt(num_guest.getText());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number of guests.");
            if(totalPriceLabel != null) totalPriceLabel.setText("Please enter a valid number of guests.");
            return;
        }

        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            System.err.println("Invalid Dates selected.");
            if(totalPriceLabel != null) totalPriceLabel.setText("Please select valid check-in and check-out dates.");
            return;
        }

        if (UserSession.getUserId() <= 0) {
            System.err.println("Error: No valid user logged in.");
            return;
        }

        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalAmount = nights * pricePerNight;

        String sql = "INSERT INTO Bookings (property_id, guest_id, check_in_date, check_out_date, total_price, booking_status, guest_count) " +
                "VALUES (?, ?, ?, ?, ?, 'pending', ?)";

        try (Connection conn = DatabaseDesign.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, 1);
            pstmt.setInt(2, UserSession.getUserId());
            pstmt.setDate(3, java.sql.Date.valueOf(checkIn));
            pstmt.setDate(4, java.sql.Date.valueOf(checkOut));
            pstmt.setDouble(5, totalAmount);
            pstmt.setInt(6, numGuests);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newID = rs.getInt(1);
                        System.out.println("RESERVATION SUCCESSFUL! ID: " + newID);
                    }
                }
                root = FXMLLoader.load(getClass().getResource("/fxml/GuestAccountView.fxml"));
                switchScene(event);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
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



