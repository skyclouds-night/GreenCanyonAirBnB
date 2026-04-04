package ph.edu.dlsu.greencanyonairbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ph.edu.dlsu.greencanyonairbnb.entity.Payment;

import java.lang.ScopedValue;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    ScopedValue<Object> findByPaymongoSourceId(String sourceId);
}