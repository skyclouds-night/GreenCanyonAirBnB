package ph.edu.dlsu.greencanyonairbnb.model.service;

import org.springframework.web.multipart.MultipartFile;
import ph.edu.dlsu.greencanyonairbnb.model.Property;

import java.math.BigDecimal;

public interface PropertyServiceInt {

    Property addNewProperty(MultipartFile photo, String propertyType, BigDecimal propertyPrice);
}
