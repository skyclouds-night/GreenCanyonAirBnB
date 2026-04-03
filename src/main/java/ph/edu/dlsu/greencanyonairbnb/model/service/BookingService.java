package ph.edu.dlsu.greencanyonairbnb.model.service;

import org.springframework.stereotype.Service;
import ph.edu.dlsu.greencanyonairbnb.model.BookedProperty;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.exception.InvalidBookingRequestException;
import ph.edu.dlsu.greencanyonairbnb.model.exception.ResourceNotFoundException;
import ph.edu.dlsu.greencanyonairbnb.model.repository.BookingRepository;


import java.util.List;

@Service
public class BookingService implements BookingServiceInt {

    private final BookingRepository bookingRepository;
    private final PropertyServiceInt propertyService;

    public List<BookedProperty> getAllBookingsByPropertyID(Long propertyID) {
        return bookingRepository.findByPropertyID(propertyID);
    }

    @Override
    public void cancelBooking(long bookingID) {
        bookingRepository.deleteById(bookingID);

    }

    @Override
    public List<BookedProperty> getAllBookingsByPropertyID(long propertyID) {
        return bookingRepository.findByPropertyID(propertyID);
    }

    @Override
    public String saveBooking(long propertyID, BookedProperty bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-In date must come before Check-out date");
        }
        Property property = propertyService.getPropertyByID(propertyID).get();
        List<BookedProperty> existingBookings = property.getBookings();
        boolean propertyAvailable = propertyAvailable(bookingRequest,existingBookings);
        if(propertyAvailable){
            property.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        } else {
            throw new InvalidBookingRequestException("Sorry, Property is not available for the selected dates." +
                    "Please select another dates." );
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    private boolean propertyAvailable(BookedProperty bookingRequest, List<BookedProperty> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                    bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))

                );
    }

    @Override
    public BookedProperty findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new ResourceNotFoundException("No booking found with code:" +confirmationCode));
    }

    @Override
    public List<BookedProperty> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedProperty> getBookingsByUserEmail(String guestEmail) {
        return bookingRepository.findByGuestEmail(guestEmail);
    }
}
