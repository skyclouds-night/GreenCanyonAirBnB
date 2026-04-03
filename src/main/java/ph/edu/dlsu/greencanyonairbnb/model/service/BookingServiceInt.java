package ph.edu.dlsu.greencanyonairbnb.model.service;

import ph.edu.dlsu.greencanyonairbnb.model.BookedProperty;

import java.util.List;

public interface BookingServiceInt {
    void cancelBooking(long bookingID);

    List<BookedProperty> getAllBookingsByPropertyID(long propertyID);

    String saveBooking(long propertyID, BookedProperty bookingRequest);

    BookedProperty findByBookingConfirmationCode(String confirmationCode);

    List<BookedProperty> getAllBookings();

    List<BookedProperty> getBookingsByUserEmail(String guestEmail);


}
