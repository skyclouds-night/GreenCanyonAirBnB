package ph.edu.dlsu.greencanyonairbnb.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ph.edu.dlsu.greencanyonairbnb.model.BookedProperty;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookedProperty, Long> {

    List<BookedProperty> findByPropertyID(Long propertyID);

    Optional<BookedProperty> findByBookingConfirmationCode(String confirmationCode);

    List<BookedProperty> findByGuestEmail(String guestEmail);
}
