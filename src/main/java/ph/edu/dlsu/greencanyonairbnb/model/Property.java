package ph.edu.dlsu.greencanyonairbnb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "app_properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long propertyId;
    private String propertyType;
    private String propertyName;
    private String propertyDetails;
    private BigDecimal propertyPrice;
    private boolean isBooked = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Image image;

    @Transient
    private String base64Image;
}