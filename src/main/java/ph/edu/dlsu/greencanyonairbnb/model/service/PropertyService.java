package ph.edu.dlsu.greencanyonairbnb.model.service;

import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.Property;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.Blob;

public class PropertyService implements PropertyServiceInt{

    @Override
    public Property addNewProperty(MultipartFile photo, String propertyType, BigDecimal propertyPrice){
        Property property = new Property();
        property.setPropertyType(propertyType);
        property.setPropertyPrice(propertyPrice);

//        if (!file.isEmpty()){
//            byte[] photoBytes = file.getBytes();
//            Blob photoBlob = new SerialBlob(photoBytes);
//        }
//        return null;
    }
}
