package ph.edu.dlsu.greencanyonairbnb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String propertyType;
    private BigDecimal propertyPrice;
    private boolean isBooked = false;

    @Lob
    private Blob photo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedProperty> bookings;

    public Property() {
         this.bookings = new ArrayList<>();
    }

    public void addBooking(BookedProperty booking) {
    if (bookings == null){
    bookings = new ArrayList<>();
    }
    bookings.add(booking);
     booking.setProperty(this);
    isBooked = true;
    String bookingCode = RandomStringUtils.randomNumeric(10);
    booking.setBookingConfirmationCode(bookingCode);

    }
}