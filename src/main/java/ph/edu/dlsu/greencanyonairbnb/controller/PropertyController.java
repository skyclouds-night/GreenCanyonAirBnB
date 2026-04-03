package ph.edu.dlsu.greencanyonairbnb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.BookedProperty;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.exception.PhotoRetrieverException;
import ph.edu.dlsu.greencanyonairbnb.exception.ResourceNotFoundException;
import ph.edu.dlsu.greencanyonairbnb.response.BookingResponse;
import ph.edu.dlsu.greencanyonairbnb.response.PropertyResponse;
import ph.edu.dlsu.greencanyonairbnb.service.BookingService;
import ph.edu.dlsu.greencanyonairbnb.service.PropertyServiceInt;
import javax.sql.rowset.serial.SerialBlob;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyServiceInt propertyService;
    private final BookingService bookingService;

    @PostMapping("/add-property")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOST') ")
    public ResponseEntity<PropertyResponse> addNewProperty(@RequestParam("photo") MultipartFile photo, @RequestParam("propertyType") String propertyType, @RequestParam("propertyPrice") BigDecimal propertyPrice) throws SQLException, IOException {

        Property savedProperty = propertyService.addNewProperty(photo, propertyType, propertyPrice);
        PropertyResponse response = new PropertyResponse(savedProperty.getPropertyID(), savedProperty.getPropertyType(), savedProperty.getPropertyPrice());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-properties")
    public ResponseEntity<List<PropertyResponse>> getAllProperties(){
        List<Property> properties = propertyService.getAllProperties();
        List<PropertyResponse> propertyResponses = new ArrayList<>();
        for (Property property: properties){
            byte[] photoBytes = propertyService.getPropertyPhotoByPropertyID(property.getPropertyID());
            if(photoBytes != null && photoBytes.length >0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                PropertyResponse propertyResponse = getPropertyResponse(property);
                propertyResponse.setPhoto(base64Photo);
                propertyResponses.add(propertyResponse);
            }

        }
        return ResponseEntity.ok(propertyResponses);
    }

    @GetMapping("/property-types")
    public List<String> getPropertyType(){
        return propertyService.getAllPropertyTypes();
    }

    @DeleteMapping("/delete/property/{propertyID}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOST') ")
    public ResponseEntity<Void> deleteProperty(@PathVariable long propertyID){
        propertyService.deleteProperty(propertyID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{propertyID}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOST') ")
    public ResponseEntity<PropertyResponse> updateProperty(@PathVariable long propertyID, @RequestParam(required = false) String propertyType, @RequestParam(required = false) BigDecimal propertyPrice, @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
        byte[] photoBytes = photo != null && !photo.isEmpty()? photo.getBytes(): propertyService.getPropertyPhotoByPropertyID(propertyID);
    Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
    Property theProperty = propertyService.updateProperty(propertyID,propertyType,propertyPrice, photoBytes);
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
        List<BookingResponse> bookingInfo = bookings.stream().map(booking -> new BookingResponse(booking.getBookingID(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = property.getPhoto();
        if (photoBlob != null){
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch(SQLException e){
                throw new PhotoRetrieverException("Error retrieving photo.");
            }
        }
        return new PropertyResponse(property.getPropertyID(), property.getPropertyType(), property.getPropertyPrice(), property.isBooked(), photoBytes,bookingInfo);
    }

    private List<BookedProperty> getAllBookingsByPropertyID(Long propertyID) {
        return bookingService.getAllBookingsByPropertyID(propertyID);
    }

    @GetMapping("/available-properties")
    public ResponseEntity<List<PropertyResponse>> getAvailableProperties(@RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                                         @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                                                         @RequestParam("propertyType") String propertyType) throws SQLException {
        List<Property> availableProperties = propertyService.getAvailableProperties(checkInDate, checkOutDate, propertyType);
        List<PropertyResponse> propertyResponses = new ArrayList<>();
        for (Property property: availableProperties){
            byte[] photoBytes = propertyService.getPropertyPhotoByPropertyID(property.getPropertyID());
            if (photoBytes != null && photoBytes.length > 0){
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                PropertyResponse propertyResponse = getPropertyResponse(property);
                propertyResponse.setPhoto(photoBase64);
                propertyResponses.add(propertyResponse);
            }
        }
        if(propertyResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(propertyResponses);
        }
    }
}
