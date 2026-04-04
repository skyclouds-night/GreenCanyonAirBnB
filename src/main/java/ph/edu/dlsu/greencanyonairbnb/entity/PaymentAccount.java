package ph.edu.dlsu.greencanyonairbnb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Payment_Accounts")
public class PaymentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "paymongo_source_id")
    private String paymongoSourceId;

    @Column(name = "paymongo_public_key")
    private String paymongoPublicKey;

    @Column(name = "paymongo_secret_key")
    private String paymongoSecretKey;

    @Lob
    @Column(name = "qr_image")
    private byte[] qrImage;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public PaymentAccount() {}

}