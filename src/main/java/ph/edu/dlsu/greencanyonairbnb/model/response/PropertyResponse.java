package ph.edu.dlsu.greencanyonairbnb.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Base64Util;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class PropertyResponse {
    private Long propertyID;
    private String propertyType;
    private BigDecimal propertyPrice;
    private boolean isBooked = false;
    private String photo;
    private List<BookingResponse> bookings;

    public PropertyResponse(Long propertyID, String propertyType, BigDecimal propertyPrice) {
        this.propertyID = propertyID;
        this.propertyType = propertyType;
        this.propertyPrice = propertyPrice;
    }

    public PropertyResponse(Long propertyID, String propertyType, BigDecimal propertyPrice, boolean isBooked, byte[] photoBytes, List<BookingResponse> bookings) {
        this.propertyID = propertyID;
        this.propertyType = propertyType;
        this.propertyPrice = propertyPrice;
        this.isBooked = isBooked;
        //this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes): null;
        this.bookings = bookings;
    }
}
