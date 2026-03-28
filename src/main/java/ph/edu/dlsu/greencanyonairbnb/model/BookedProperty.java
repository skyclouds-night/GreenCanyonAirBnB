package ph.edu.dlsu.greencanyonairbnb.model;

import java.time.*;


public class BookedProperty {

    private long bookingID;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestName;
    private String guestEmail;
    private int NumofGuests;
    private String bookingConfirmationCode;
    private Property property;

    public BookedProperty(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
