package sit.int221.eventsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.entities.CategoryOwner;
import sit.int221.eventsservice.entities.CategoryOwnerId;
import sit.int221.eventsservice.entities.User;

import java.util.List;

public interface CategoryOwnerRepository extends JpaRepository<CategoryOwner, Integer> {
    List<CategoryOwner> findByUserId(User id);

    CategoryOwner findByEventCategoryIdAndUserId(Category categoryId, User Id);
}