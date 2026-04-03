package ph.edu.dlsu.greencanyonairbnb.service;

import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.Property;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PropertyServiceInt {

    Property addNewProperty(MultipartFile photo, String propertyType, BigDecimal propertyPrice);

    List<String> getAllPropertyTypes();

    List<Property> getAllProperties();

    byte[] getPropertyPhotoByPropertyID(Long propertyID) throws SQLException;

    void deleteProperty(long propertyID);

    Property updateProperty(long propertyID, String propertyType, BigDecimal propertyPrice, byte[] photoBytes);

    Optional<Property> getPropertyByID(long propertyID);

    List<Property> getAvailableProperties(LocalDate checkInDate, LocalDate checkOutDate, String propertyType);
}
