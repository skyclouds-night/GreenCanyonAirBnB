package ph.edu.dlsu.greencanyonairbnb.model.service;

import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.repository.PropertyRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface PropertyServiceInt {

    Property addNewProperty(MultipartFile photo, String propertyType, BigDecimal propertyPrice);

    byte[] getPropertyPhotobyPropertyID(Long propertyID);

    @Override
    public void deleteProperty(long propertyID){
        Optional<Property> theProperty = PropertyRepository.findByID(propertyID);
        if(theProperty.isPresent()){
            PropertyRepository.deleteByID(propertyID);
        }
    }

    @Override
    void deleteProperty(long propertyID);

    Property updateProperty(long propertyID, String propertyType, BigDecimal propertyPrice, byte[] photoBytes);

    Optional<Property> getPropertyByID(long propertyID);
}
