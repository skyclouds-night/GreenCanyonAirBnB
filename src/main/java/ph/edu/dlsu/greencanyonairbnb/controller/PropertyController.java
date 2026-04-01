package ph.edu.dlsu.greencanyonairbnb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.BookedProperty;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.exception.PhotoRetrieverException;
import ph.edu.dlsu.greencanyonairbnb.model.exception.ResourceNotFoundException;
import ph.edu.dlsu.greencanyonairbnb.model.response.BookingResponse;
import ph.edu.dlsu.greencanyonairbnb.model.response.PropertyResponse;
import ph.edu.dlsu.greencanyonairbnb.model.service.BookingService;
import ph.edu.dlsu.greencanyonairbnb.model.service.PropertyService;
import ph.edu.dlsu.greencanyonairbnb.model.service.PropertyServiceInt;
import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @PutMapping("/update/{propertyID}")
    public ResponseEntity<PropertyResponse> updateProperty(@PathVariable long propertyID, @RequestParam(required = false) String propertyType, @RequestParam(required = false) BigDecimal propertyPrice, @RequestParam(required = false) MultipartFile photo){
        byte[] photoBytes = photo != null && !photo.isEmpty()? photo.getBytes(): PropertyService.getPropertyPhotoByPropertyID(propertyID);
    Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
    Property theProperty = PropertyService.updateProperty(propertyID,propertyType,propertyPrice, photoBytes);
    theProperty.setPhoto(photoBlob);
    PropertyResponse propertyResponse = getPropertyResponse(theProperty);
    return ResponseEntity.ok(propertyResponse);
    }

    @GetMapping("/property/{propertyID}")
    public ResponseEntity<Optional<PropertyResponse>> getPropertyByID (@PathVariable long propertyID){
        Optional<Property> theProperty = propertyService.getPropertyByID(propertyID);
        return theProperty.map(property -> {
            PropertyResponse propertyResponse = getPropertyResponse(property);
            return ResponseEntity.ok(Optional.of(propertyResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Property not Found."));
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
