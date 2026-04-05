package ph.edu.dlsu.greencanyonairbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ph.edu.dlsu.greencanyonairbnb.model.Property;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("Select Distinct p.propertyType from Property p")
    List<String> findDistinctPropertyTypes();

    @Query("Select p from Property p " +
            "Where p.propertyType like %:propertyType% " +
            "And p.ID not in (" +
            "Select bp.property.id from BookedProperty bp " +
            "Where ((bp.checkInDate <= :checkOutDate) and (bp.checkOutDate >= :checkInDate))" +
            ")")

    List<Property> findAvailablePropertiesByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String propertyType);

}
