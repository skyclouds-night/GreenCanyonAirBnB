package ph.edu.dlsu.greencanyonairbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ph.edu.dlsu.greencanyonairbnb.model.Property;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByUserId(Long userId);

    Optional<Property> findByPropertyName(String name);
    List<Property> findPropertiesByIsBooked(boolean isBooked);
    List<Property> findAllByUserId(Long userId);

}
