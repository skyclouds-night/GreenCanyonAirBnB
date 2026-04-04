package ph.edu.dlsu.greencanyonairbnb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long bookingId;
    private Long accountId;

    private Double amount;

    private String status; // pending, completed

    private String paymongoSourceId;

    private String paymongoPaymentId;

    public void setStatus(String completed) {
    }

    public void setBookingId(Long bookingId) {
    }

    public void setPaymentStatus(String completed) {
    }

    public void setPaymongoStatus(String status) {
    }

    public void setAccountId(Long accountId) {
    }

    public void setAmount(double amount) {
    }

    public void setPaymentMethod(String qrPh) {
    }

    public void setPaymongoSourceId(String sourceId) {
    }
}