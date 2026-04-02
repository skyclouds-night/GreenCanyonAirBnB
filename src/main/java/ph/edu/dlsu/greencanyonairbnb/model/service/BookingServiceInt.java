package ph.edu.dlsu.greencanyonairbnb.model.service;

import ph.edu.dlsu.greencanyonairbnb.model.BookedProperty;

import java.util.List;

public interface BookingServiceInt {
    public void cancelBooking(long bookingID);

    public String saveBooking(long propertyID, BookedProperty bookingRequest);

    public BookedProperty findByBookingConfirmationCode(String confirmationCode);

    public List<BookedProperty> getAllBookings();

}
