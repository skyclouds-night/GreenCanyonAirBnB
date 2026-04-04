package ph.edu.dlsu.greencanyonairbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ph.edu.dlsu.greencanyonairbnb.entity.PaymentAccount;

import java.util.Optional;

public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {

    Optional<PaymentAccount> findByAdminIdAndIsActiveTrue(Long adminId);

}