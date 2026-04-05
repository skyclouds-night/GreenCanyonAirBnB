package ph.edu.dlsu.greencanyonairbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ph.edu.dlsu.greencanyonairbnb.model.BookedProperty;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookedProperty, Long> {


    List<BookedProperty> findByPropertyId(Long propertyID);
    Optional<BookedProperty> findByBookingConfirmationCode(String confirmationCode);

    List<BookedProperty> findByGuestEmail(String guestEmail);
}
