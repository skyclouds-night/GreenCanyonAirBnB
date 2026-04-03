package ph.edu.dlsu.greencanyonairbnb.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import java.util.List;
import java.time.LocalDate;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("Select Distinct p.propertyType from Property p")
    List<String> findDistinctPropertyTypes();

    @Query("Select p from Property p" +
            "Where p.propertyType like %:propertyType%" +
            "And p.ID not in (" +
            "Select bp.property.id from BookedProperty bp" +
            "Where ((bp.checkInDate <= :checkOutDate) and (bp.checkOutDate >= :checkInDate))" +
            ")")

    List<Property> findAvailableProperties(LocalDate checkInDate, LocalDate checkOutDate, String propertyType);

}
