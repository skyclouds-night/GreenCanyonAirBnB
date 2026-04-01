package ph.edu.dlsu.greencanyonairbnb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.BookedProperty;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.exception.PhotoRetrieverException;
import ph.edu.dlsu.greencanyonairbnb.model.response.BookingResponse;
import ph.edu.dlsu.greencanyonairbnb.model.response.PropertyResponse;
import ph.edu.dlsu.greencanyonairbnb.model.service.BookingService;
import ph.edu.dlsu.greencanyonairbnb.model.service.PropertyServiceInt;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PropertyController {

    private final PropertyServiceInt propertyService;
    private final BookingService bookingService;

    public ResponseEntity<PropertyResponse> addNewProperty(@RequestParam("photo") MultipartFile photo, @RequestParam("propertyType") String propertyType, @RequestParam("propertyPrice") BigDecimal propertyPrice) {

        Property savedProperty = propertyService.addNewProperty(photo, propertyType, propertyPrice);
        PropertyResponse response = new PropertyResponse(savedProperty.getPropertyID(), savedProperty.getPropertyType(), savedProperty.getPropertyPrice());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<PropertyResponse>> getAllProperties(){
        List<Property> properties = propertyService.getAllProperties();
        List<PropertyResponse> propertyResponses = new ArrayList<>();
        for (Property property: properties){
            byte[] photoBytes = propertyService.getPropertyPhotobyPropertyID(property.getPropertyID());
            if(photoBytes != null && photoBytes.length >0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                PropertyResponse propertyResponse = getPropertyResponse(property);
                propertyResponse.setPhoto(base64Photo);
                propertyResponses.add(propertyResponse);
            }

        }
        return ResponseEntity.ok(propertyResponse);
    }
    //

    @DeleteMapping("/delete/property/{propertyID}")
    public ResponseEntity<Void> deleteProperty(@PathVariable long propertyID){
        propertyService.deleteProperty(propertyID);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private PropertyResponse getPropertyResponse(Property property) {

        List<BookedProperty> bookings = getAllBookingsByPropertyID(property.getPropertyID());
        List<BookingResponse> bookingInfo = bookings.stream().map(bookings -> new BookingResponse(bookings.getBookingID(), bookings.getCheckInDate(), bookings.getCheckOutDate(), bookings.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = property.getPhoto();
        if (photoBlob != null){
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length);
            } catch(SQLException e){
                throw new PhotoRetrieverException("Error retrieving photo.");
            }
        }
        return new PropertyResponse(property.getPropertyID(), property.getPropertyType(), property.getPropertyPrice(), property.isBooked(), photoBytes,bookingInfo);
    }

    private List<BookedProperty> getAllBookingsByPropertyID(Long propertyID) {
        return bookingService.getAllBookingsByPropertyID(propertyID);
    }
}
