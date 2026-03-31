package ph.edu.dlsu.greencanyonairbnb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.response.PropertyResponse;
import ph.edu.dlsu.greencanyonairbnb.model.service.PropertyServiceInt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PropertyController {

    private PropertyServiceInt propertyService;

    public ResponseEntity<PropertyResponse> addNewProperty(@RequestParam("photo") MultipartFile photo, @RequestParam("propertyType") String propertyType, @RequestParam("propertyPrice") BigDecimal propertyPrice) {

        Property savedProperty = propertyService.addNewProperty(photo, propertyType, propertyPrice);
        PropertyResponse response = new PropertyResponse(savedProperty.getPropertyID(), savedProperty.getPropertyType(), savedProperty.getPropertyPrice());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<PropertyResponse>> getAllProperties(){
        List<Property> properties = propertyService.getAllProperties();
        List<PropertyResponse> propertyResponses = new ArrayList<>();
        for (Property property: properties){
            byte[] photoBytes = propertyService.getPropertyPhotobyPropertyID(property.getId);
            if(photoBytes != null && photoBytes.length >0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                PropertyResponse propertyResponse = getPropertyResponse(property);
                propertyResponse.setPhoto(base64Photo);
                propertyResponses.add(propertyResponse);
            }

        }
        return ResponseEntity.ok(propertyResponse);
    }
}
