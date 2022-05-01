package sit.int221.eventsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.eventsservice.entities.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
