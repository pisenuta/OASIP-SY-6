package sit.int221.eventsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.eventsservice.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
