package ph.edu.dlsu.greencanyonairbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ph.edu.dlsu.greencanyonairbnb.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String role);

    boolean existsByName(String role);
}
