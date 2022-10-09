package sit.int221.eventsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.eventsservice.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByName(String name);

    boolean existsByEmail(String email);
}