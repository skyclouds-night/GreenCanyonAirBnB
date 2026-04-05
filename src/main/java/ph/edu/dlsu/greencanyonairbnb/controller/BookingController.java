package ph.edu.dlsu.greencanyonairbnb.controller;


import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

import org.springframework.web.bind.annotation.*;
import ph.edu.dlsu.greencanyonairbnb.model.BookedProperty;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.exception.InvalidBookingRequestException;
import ph.edu.dlsu.greencanyonairbnb.exception.ResourceNotFoundException;
import ph.edu.dlsu.greencanyonairbnb.response.BookingResponse;
import ph.edu.dlsu.greencanyonairbnb.response.PropertyResponse;
import ph.edu.dlsu.greencanyonairbnb.service.BookingServiceInt;
import ph.edu.dlsu.greencanyonairbnb.service.PropertyServiceInt;



@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingServiceInt bookingService;
    private final PropertyServiceInt propertyService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
       List<BookedProperty> bookings = bookingService.getAllBookings();
       List<BookingResponse> bookingResponses = new ArrayList<>();
       for (BookedProperty booking : bookings){
           BookingResponse bookingResponse = getBookingResponse(booking);
           bookingResponses.add(bookingResponse);
       }
       return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/booking-confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try {
            BookedProperty booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("user/{guestEmail}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String guestEmail){
       List<BookedProperty> bookings = bookingService.getBookingsByUserEmail(guestEmail);
        List<BookingResponse> bookingResponses = bookingService.getBookingsByUserEmail(guestEmail)
                .stream()
                .map(this::getBookingResponse)
                .toList();
       return ResponseEntity.ok(bookingResponses);
    }


    @PostMapping("/property/{propertyID}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable long propertyID, @RequestBody BookedProperty bookingRequest){
        try {
            String confirmationCode = bookingService.saveBooking(propertyID, bookingRequest);
            return ResponseEntity.ok("Property booked successfully. Your booking confirmation code is:" + confirmationCode);

        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingID}/delete")
    public void cancelBooking( @PathVariable long bookingID){
        bookingService.cancelBooking(bookingID);
    }

    private BookingResponse getBookingResponse(BookedProperty booking) {
        Optional<Property> theProperty = propertyService.getPropertyByID(booking.getProperty().getId());
        if(theProperty.isPresent()){
            PropertyResponse property = new PropertyResponse(theProperty.get().getId(), theProperty.get().getPropertyType(), theProperty.get().getPropertyPrice());
            return new BookingResponse(booking.getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getGuestName(), booking.getGuestEmail(), booking.getNumofGuests(), booking.getBookingConfirmationCode(), property);
        }
        return new BookingResponse();

    }
}
