package ph.edu.dlsu.greencanyonairbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ph.edu.dlsu.greencanyonairbnb.model.Property;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Property, Long> {

    byte[] findByPropertyId(Long propertyId);
}

