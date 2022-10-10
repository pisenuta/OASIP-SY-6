package sit.int221.eventsservice.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.CustomAccessDeniedHandler;
import sit.int221.eventsservice.advice.HandleExceptionBadRequest;
import sit.int221.eventsservice.advice.HandleExceptionForbidden;
import sit.int221.eventsservice.advice.OverlappedExceptionHandler;
import sit.int221.eventsservice.dtos.Event.EventDTO;
import sit.int221.eventsservice.entities.Event;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.entities.Role;
import sit.int221.eventsservice.entities.User;
import sit.int221.eventsservice.repositories.EventRepository;
import sit.int221.eventsservice.repositories.UserRepository;

@AllArgsConstructor
@Service
public class EventService {

    private final EventRepository repository;

    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public EventDTO getEventById(Integer id) throws HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            Event event = this.repository.findById(id).orElseThrow(() -> {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
            });
            return this.modelMapper.map(event, EventDTO.class);
        } else if (userLogin.getRole().equals(Role.student)) {
            Event event = this.repository.findById(id).orElseThrow(() -> {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
            });
            if (Objects.equals(event.getUser().getEmail(), userLogin.getEmail())) {
                return this.modelMapper.map(event, EventDTO.class);
            } else {
                throw new HandleExceptionForbidden("You are not allowed to access this event");
            }
        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Event> eventListByCategoryOwner = repository.findEventCategoryOwnerByEmail(userLogin.getEmail());
            Event eveventListByCategoryOwnerent = this.repository.findById(id).orElseThrow(() -> {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
            });
            if (eventListByCategoryOwner.contains(eveventListByCategoryOwnerent)) {
                return this.modelMapper.map(eveventListByCategoryOwnerent, EventDTO.class);
            }
            throw new HandleExceptionForbidden("You are not allowed to access this event");
        }
        return null;
    }

    public List<EventDTO> getAllEvent() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains(Role.admin.name()));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            return this.listMapper.mapList(this.repository.findAll(Sort.by("eventStartTime").descending()), EventDTO.class, this.modelMapper);

        } else if (userLogin.getRole().equals(Role.student)) {
            return this.listMapper.mapList(this.repository.findByBookingEmail(userLogin.getEmail(), Sort.by("eventStartTime").descending()), EventDTO.class, this.modelMapper);

        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Event> eventListByCategoryOwner = repository.findEventCategoryOwnerByEmail(userLogin.getEmail());
            return listMapper.mapList(eventListByCategoryOwner, EventDTO.class, modelMapper);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userLogin.getEmail() + "is not owner of this event");
        }
    }

    public Event save(EventDTO newEvent) throws OverlappedExceptionHandler, HandleExceptionForbidden, HandleExceptionBadRequest {
        Date newEventStartTime = Date.from(newEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(newEvent.getEventStartTime()), newEvent.getEventDuration());
        List<EventDTO> eventList = getAllEvent();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());

        if (userLogin.getRole().equals(Role.admin)) {
            for (EventDTO eventDTO : eventList) {
                if (Objects.equals(newEvent.getEventCategory().getId(), eventDTO.getEventCategory().getId())) { //เช็คเฉพาะ EventCategory เดียวกัน
                    Date eventStartTime = Date.from(eventDTO.getEventStartTime());
                    Date eventEndTime = findEndDate(Date.from(eventDTO.getEventStartTime()), eventDTO.getEventDuration());
                    if (newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventStartTime) ||
                            newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventEndTime) ||
                            newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventEndTime) ||
                            newEventStartTime.after(eventStartTime) && newEventEndTime.before(eventEndTime) ||
                            newEventStartTime.equals(eventStartTime)) {
                        throw new OverlappedExceptionHandler("Time is Overlapped");
                    }
                }
            }

            User addByAdmin = userRepository.findByEmail(newEvent.getBookingEmail());
            newEvent.setUserId(addByAdmin.getUserId());
            Event e = modelMapper.map(newEvent, Event.class);
            repository.saveAndFlush(e);
            return ResponseEntity.status(HttpStatus.OK).body(e).getBody();

        } else if (userLogin.getRole().equals(Role.student)) {
            if (Objects.equals(userLogin.getEmail(), newEvent.getBookingEmail())) {
                for (EventDTO eventDTO : eventList) {
                    if (Objects.equals(newEvent.getEventCategory().getId(), eventDTO.getEventCategory().getId())) { //เช็คเฉพาะ EventCategory เดียวกัน
                        Date eventStartTime = Date.from(eventDTO.getEventStartTime());
                        Date eventEndTime = findEndDate(Date.from(eventDTO.getEventStartTime()), eventDTO.getEventDuration());
                        if (newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventStartTime) ||
                                newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventEndTime) ||
                                newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventEndTime) ||
                                newEventStartTime.after(eventStartTime) && newEventEndTime.before(eventEndTime) ||
                                newEventStartTime.equals(eventStartTime)) {
                            throw new OverlappedExceptionHandler("Time is Overlapped");
                        }
                    }
                }
                newEvent.setUserId(userLogin.getUserId());
                Event e = modelMapper.map(newEvent, Event.class);
                repository.saveAndFlush(e);
                return ResponseEntity.status(HttpStatus.OK).body(e).getBody();
            } else {
                throw new HandleExceptionBadRequest("The booking email must be the same as the student's email");
            }
        } else {
            throw new HandleExceptionForbidden("You are not allowed to add event");
        }
    }

    public Date findEndDate(Date date, Integer duration) {
        return new Date(date.getTime() + (duration * 60000 + 60000));
    }

    public List<EventDTO> getEventByCategoryId(Category eventCategoryId) throws HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            List<Event> eventByCategory = repository.findAllByEventCategoryOrderByEventCategoryDesc(eventCategoryId);
            return listMapper.mapList(eventByCategory, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.student)) {
            List<Event> eventByCategory = repository.
                    findAllByBookingEmailAndEventCategoryOrderByEventCategoryDesc(auth.getPrincipal().toString(), eventCategoryId);
            return listMapper.mapList(eventByCategory, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Integer> eventByCategory = userLogin.getEventCategories().stream().map(Category::getId).collect(Collectors.toList());
            if (eventByCategory.contains(eventCategoryId.getId())) {
                List<Event> eventByCategoryId = repository.findAllByEventCategoryOrderByEventCategoryDesc(eventCategoryId);
                return listMapper.mapList(eventByCategoryId, EventDTO.class, modelMapper);
            } else {
                throw new HandleExceptionForbidden("You are not owner of this category");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userLogin.getEmail() + "is not owner of this event");
        }
    }

    public List<EventDTO> getPastEvent(Instant instant) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            List<Event> pastEvent = repository.findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.student)) {
            List<Event> pastEvent =
                    repository.findAllByBookingEmailAndEventStartTimeBeforeOrderByEventStartTimeDesc(auth.getPrincipal().toString(), instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Integer> eventByCategory = userLogin.getEventCategories().stream().map(Category::getId).collect(Collectors.toList());
            List<Event> pastEvent = repository.findAllByEventCategory_IdInAndEventStartTimeBeforeOrderByEventStartTimeDesc(eventByCategory, instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userLogin.getEmail() + "is not owner of this event");
        }
    }

    public List<EventDTO> getUpcomingEvent(Instant instant) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            List<Event> pastEvent = repository.findAllByEventStartTimeAfterOrderByEventStartTimeAsc(instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.student)) {
            List<Event> pastEvent = repository.
                    findAllByBookingEmailAndEventStartTimeAfterOrderByEventStartTimeAsc(auth.getPrincipal().toString(), instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Integer> eventByCategory = userLogin.getEventCategories().stream().map(Category::getId).collect(Collectors.toList());
            List<Event> pastEvent = repository.findAllByEventCategory_IdInAndEventStartTimeAfterOrderByEventStartTimeAsc(eventByCategory, instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userLogin.getEmail() + "is not owner of this event");
        }
    }

    public List<EventDTO> getEventByDateTime(String startTime, String endTime) throws HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            List<Event> eventByDateTime = repository.findAllByEventStartTimeBetween(Instant.parse(startTime), Instant.parse(endTime));
            return listMapper.mapList(eventByDateTime, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.student)) {
            List<Event> eventByDateTime = repository.
                    findAllByBookingEmailAndEventStartTimeBetween(auth.getPrincipal().toString(), Instant.parse(startTime), Instant.parse(endTime));
            return listMapper.mapList(eventByDateTime, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Event> eventList = repository.findByEventCategory_IdInAndEventStartTimeBetween(userLogin.getEventCategories().stream().map(Category::getId).collect(Collectors.toList()),
                    Instant.parse(startTime), Instant.parse(endTime));
            if(eventList != null){
                return listMapper.mapList(eventList, EventDTO.class, modelMapper);
            } else {
                throw new HandleExceptionForbidden("You are not category owner of this event");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userLogin.getEmail() + "is not owner of this event");
        }
    }
}


