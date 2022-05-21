package sit.int221.eventsservice.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int221.eventsservice.dtos.SimpleEventDTO;
import sit.int221.eventsservice.entities.Event;
import sit.int221.eventsservice.entities.Eventcategory;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    
    List<Event> findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(Instant instantTime);

    List<Event> findAllByEventStartTimeAfterOrderByEventStartTimeAsc(Instant instantTime);

    List<Event> findAllByEventCategoryOrderByEventCategoryDesc(Eventcategory eventCategory);

    List<Event> findAllByEventStartTimeBetween(Instant starTime, Instant endTime);

//    List<Event> findAllByEventCategory(Integer eventCategoryId);
}
