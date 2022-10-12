package sit.int221.eventsservice.repositories;

import net.bytebuddy.TypeCache;
import net.bytebuddy.matcher.MethodSortMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.eventsservice.entities.Event;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.entities.User;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(Instant instantTime);
    List<Event> findAllByBookingEmailAndEventStartTimeBeforeOrderByEventStartTimeDesc(String bookingEmail, Instant eventStartTime);

    List<Event> findAllByEventStartTimeAfterOrderByEventStartTimeAsc(Instant instantTime);
    List<Event> findAllByBookingEmailAndEventStartTimeAfterOrderByEventStartTimeAsc(String bookingEmail,Instant instantTime);

    List<Event> findAllByEventCategoryOrderByEventCategoryDesc(Category eventCategory);
    List<Event> findAllByBookingEmailAndEventCategoryOrderByEventCategoryDesc(String bookingEmail,Category eventCategory);

    List<Event> findAllByEventStartTimeBetween(Instant starTime, Instant endTime);
    List<Event> findAllByBookingEmailAndEventStartTimeBetween(String bookingEmail ,Instant starTime, Instant endTime);

    List<Event> findByBookingEmail (String email, Sort sort);

    List<Event> findByEventCategory_IdInAndEventStartTimeBetween(Collection<Integer> categoryIds, Instant starTime, Instant endTime);

    @Query(value = "SELECT e1 FROM Event e1 JOIN CategoryOwner e2 ON e1.eventCategory.id = e2.eventCategoryId.id " +
            "JOIN User u ON u.userId = e2.userId.userId WHERE u.email = :email")
    List<Event> findEventCategoryOwnerByEmail(@Param("email") String email);

    List<Event> findAllByEventCategory_IdInAndEventStartTimeBeforeOrderByEventStartTimeDesc(Collection<Integer> ids, Instant instantTime);

    List<Event> findAllByEventCategory_IdInAndEventStartTimeAfterOrderByEventStartTimeAsc(Collection<Integer> ids, Instant instantTime);
}
