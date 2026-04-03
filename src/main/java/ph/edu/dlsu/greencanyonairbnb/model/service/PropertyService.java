package ph.edu.dlsu.greencanyonairbnb.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.model.exception.InternalServerException;
import ph.edu.dlsu.greencanyonairbnb.model.exception.ResourceNotFoundException;
import ph.edu.dlsu.greencanyonairbnb.model.repository.PropertyRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyService implements PropertyServiceInt{

    private final PropertyRepository propertyRepository;

    @Override
    public Property addNewProperty(MultipartFile file, String propertyType, BigDecimal propertyPrice) throws SQLException, IOException {
        Property property = new Property();
        property.setPropertyType(propertyType);
        property.setPropertyPrice(propertyPrice);

        if (!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            property.setPhoto(photoBlob);
        }
        return propertyRepository.save(property);
    }

    @Override
    public List<String> getAllPropertyTypes() {
        return propertyRepository.findDistinctPropertyTypes();
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public byte[] getPropertyPhotoByPropertyID(Long propertyID) throws SQLException {
        Optional<Property> theProperty = propertyRepository.findById(propertyID);
        if(theProperty.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Property not found.");
        }
        Blob photoBlob = theProperty.get().getPhoto();
        if (photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteProperty(long propertyID) {
        Optional<Property> theProperty = propertyRepository.findById(propertyID);
        if(theProperty.isPresent()){
            propertyRepository.deleteById(propertyID);
        }
    }

    @Override
    public Property updateProperty(long propertyID, String propertyType, BigDecimal propertyPrice, byte[] photoBytes) {
        Property property =  propertyRepository.findById(propertyID).get();
        if (propertyType != null) property.setPropertyType(propertyType);
        if (propertyPrice != null) property.setPropertyPrice(propertyPrice);
        if (photoBytes != null && photoBytes.length > 0){
            try{
                property.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException ex){
                throw new InternalServerException("Error updating property");
            }
        }
        return propertyRepository.save(property);
    }

    @Override
    public Optional<Property> getPropertyByID(long propertyID) {
        return Optional.of(propertyRepository.findById(propertyID).get());
    }

    @Override
    public List<Property> getAvailableProperties(LocalDate checkInDate, LocalDate checkOutDate, String propertyType) {
        return propertyRepository.findAvailablePropertiesByDatesAndType(checkInDate,checkOutDate, propertyType);
    }


}
