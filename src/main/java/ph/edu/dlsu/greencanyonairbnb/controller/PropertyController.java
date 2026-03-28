package ph.edu.dlsu.greencanyonairbnb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.response.PropertyResponse;
import ph.edu.dlsu.greencanyonairbnb.model.service.PropertyServiceInt;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class PropertyController {

    private PropertyServiceInt propertyService;

    public ResponseEntity<PropertyResponse> addNewProperty(@RequestParam("photo") MultipartFile photo, @RequestParam("propertyType") String propertyType, @RequestParam("propertyPrice") BigDecimal propertyPrice) {

        Property savedProperty = propertyService.addNewProperty(photo, propertyType, propertyPrice);
        PropertyResponse response = new PropertyResponse(savedProperty.getPropertyID(), savedProperty.getPropertyType(), savedProperty.getPropertyPrice());
    }
}
