package ph.edu.dlsu.greencanyonairbnb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private long bookingID;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestName;
    private String guestEmail;
    private int NumofGuests;
    private String bookingConfirmationCode;
    private PropertyResponse property;

    public BookingResponse(long bookingID, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode) {
        this.bookingID = bookingID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

}
