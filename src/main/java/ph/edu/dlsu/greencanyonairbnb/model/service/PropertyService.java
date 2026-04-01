package ph.edu.dlsu.greencanyonairbnb.model.service;

import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.exception.ResourceNotFoundException;
import ph.edu.dlsu.greencanyonairbnb.model.repository.PropertyRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PropertyService implements PropertyServiceInt{

    @Override
    public Property addNewProperty(MultipartFile photo, String propertyType, BigDecimal propertyPrice){
        Property property = new Property();
        property.setPropertyType(propertyType);
        property.setPropertyPrice(propertyPrice);

        if (!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
        }
        return null;
    }

    @Override
    public byte[] getPropertyPhotobyPropertyID(Long propertyID) throws SQLException {
        Optional<Property> theProperty = PropertyRepository.findById(propertyID);
        if(theProperty.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Property not found.")
        }
        Blob photoBlob = theProperty.get().getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1,(int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteProperty(long propertyID) {

    }

    @Override
    public List<String> getAllPropertyTypes(){
        return propertyRepository.findDistinctPropertyTypes();
    }

    @Override
    public List<Property> getAllProperties(){
        return propertyRepository.findAll();
    }
}
