package ph.edu.dlsu.greencanyonairbnb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import ph.edu.dlsu.greencanyonairbnb.repository.PropertyRepository;

import java.util.Base64;
import java.util.List;


@Service
public class PropertyService {
    @Autowired
    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<Property> finUserProperties(Long userId) {
        return propertyRepository.findAllByUserId(userId);
    }

    public List<Property> getAllProperties() {
        List<Property> properties = propertyRepository.findAllWithImages();
        for (Property p : properties) {
            if (p.getImage() != null && p.getImage().getImgData() != null) {
                String base64 = Base64.getEncoder().encodeToString(p.getImage().getImgData());
                p.setBase64Image(base64);
            }
        }
        return properties;
    }



}
