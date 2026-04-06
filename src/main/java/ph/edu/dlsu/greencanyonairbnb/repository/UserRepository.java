package ph.edu.dlsu.greencanyonairbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ph.edu.dlsu.greencanyonairbnb.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);
}