package sit.int221.eventsservice.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sit.int221.eventsservice.advice.HandleExceptionBadRequest;
import sit.int221.eventsservice.advice.HandleExceptionForbidden;
import sit.int221.eventsservice.advice.OverlappedExceptionHandler;
import sit.int221.eventsservice.dtos.Event.EventDTO;
import sit.int221.eventsservice.dtos.Event.EventPostDTO;
import sit.int221.eventsservice.dtos.Event.EventPutDTO;
import sit.int221.eventsservice.dtos.File.FileStorageProperties;
import sit.int221.eventsservice.entities.Event;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.entities.Role;
import sit.int221.eventsservice.entities.User;
import sit.int221.eventsservice.repositories.CategoryRepository;
import sit.int221.eventsservice.repositories.EventRepository;
import sit.int221.eventsservice.repositories.UserRepository;

import javax.validation.Valid;

@AllArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private EmailService emailService;

    private FileStorageService fileStorageService;

    private final FileStorageProperties fileStorageProperties;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    public EventDTO getEventById(Integer id) throws HandleExceptionForbidden, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        Event eventById = eventRepository.findById(id).orElseThrow(() -> new HandleExceptionForbidden("Event not found"));

        if (userLogin.getRole().equals(Role.admin)) {
            Event event = this.eventRepository.findById(id).orElseThrow(() -> {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
            });

            return this.modelMapper.map(event, EventDTO.class);

        } else if (userLogin.getRole().equals(Role.student)) {
            Event event = this.eventRepository.findById(id).orElseThrow(() -> {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
            });
            if (Objects.equals(event.getUser().getEmail(), userLogin.getEmail())) {
                return this.modelMapper.map(event, EventDTO.class);
            } else {
                throw new HandleExceptionForbidden("You are not allowed to access this event");
            }

        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Event> eventListByCategoryOwner = eventRepository.findEventCategoryOwnerByEmail(userLogin.getEmail());
            Event eventListByCategoryOwner1 = this.eventRepository.findById(id).orElseThrow(() -> {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
            });
            if (eventListByCategoryOwner.contains(eventListByCategoryOwner1)) {
                return this.modelMapper.map(eventListByCategoryOwner1, EventDTO.class);
            }
            throw new HandleExceptionForbidden("You are not allowed to access this event");
        }
        throw new HandleExceptionForbidden("You are not allowed to access this event");
    }

    public List<EventDTO> getAllEvent() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains(Role.admin.name()));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            return this.listMapper.mapList(this.eventRepository.findAll(Sort.by("eventStartTime").descending()), EventDTO.class, this.modelMapper);

        } else if (userLogin.getRole().equals(Role.student)) {
            return this.listMapper.mapList(this.eventRepository.findByBookingEmail(userLogin.getEmail(), Sort.by("eventStartTime").descending()), EventDTO.class, this.modelMapper);

        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Event> eventListByCategoryOwner = eventRepository.findEventCategoryOwnerByEmail(userLogin.getEmail());
            return listMapper.mapList(eventListByCategoryOwner, EventDTO.class, modelMapper);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userLogin.getEmail() + "is not owner of this event");
        }
    }

    public List<EventDTO> getEventToCheckOverlap() {
        return this.listMapper.mapList(this.eventRepository.findAll(Sort.by("eventStartTime").descending()), EventDTO.class, this.modelMapper);
    }

    public ResponseEntity<String> deleteEvent(Integer Id) throws HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        Event eventById = eventRepository.getById(Id);

        String eventDir = eventById.getId().toString();
        String path = Paths.get(fileStorageProperties.getUploadDir() + "/" + eventDir).toString();

        if (userLogin.getRole().equals(Role.admin)) {
            eventRepository.findById(Id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            Id + " does not exist !!!"));
            eventRepository.deleteById(Id);
            fileStorageService.deleteDir(path);
            return ResponseEntity.status(200).body("Delete Event " + Id + " Successfully");
        } else if (userLogin.getRole().equals(Role.student)) {
            Event event = eventRepository.findById(Id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            Id + " does not exist !!!"));
            if (Objects.equals(event.getUser().getEmail(), userLogin.getEmail())) {
                eventRepository.deleteById(Id);
                fileStorageService.deleteDir(path);
                return ResponseEntity.status(200).body("Delete Event " + Id + " Successfully");
            } else {
                throw new HandleExceptionForbidden("You are not owner of this event");
            }
        } else {
            throw new HandleExceptionForbidden("You are not allowed to delete event");
        }
    }

    public Event save(EventPostDTO newEvent, MultipartFile file) throws OverlappedExceptionHandler, HandleExceptionForbidden, HandleExceptionBadRequest, IOException {
        Date newEventStartTime = Date.from(newEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(newEvent.getEventStartTime()), newEvent.getEventDuration());
        List<EventDTO> eventList = getEventToCheckOverlap();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());

//        String eventDir = newEvent.getId().toString();
//        Path path = Paths.get(fileStorageProperties.getUploadDir() + "/" + eventDir);
//        System.out.println(path.toString());

        if (userLogin != null) {
            if (userLogin.getRole().equals(Role.admin)) {
                checkOverlapCreate(newEvent, newEventStartTime, newEventEndTime, eventList);
                User addByAdmin = userRepository.findByEmail(newEvent.getBookingEmail());
                newEvent.setUserId(addByAdmin.getUserId());
                Event event = modelMapper.map(newEvent, Event.class);
                eventRepository.saveAndFlush(event);
                fileStorageService.storeFile(file, event);
                sendEmail(newEvent, "Your appointment is confirmed.");
                return ResponseEntity.status(HttpStatus.OK).body(event).getBody();

            } else if (userLogin.getRole().equals(Role.student)) {
                if (Objects.equals(userLogin.getEmail(), newEvent.getBookingEmail())) {
                    checkOverlapCreate(newEvent, newEventStartTime, newEventEndTime, eventList);
                    newEvent.setUserId(userLogin.getUserId());
                    Event event = modelMapper.map(newEvent, Event.class);
                    eventRepository.saveAndFlush(event);
                    fileStorageService.storeFile(file, event);
                    sendEmail(newEvent, "Your appointment is confirmed.");
                    return ResponseEntity.status(HttpStatus.OK).body(event).getBody();
                } else {
                    throw new HandleExceptionBadRequest("The booking email must be the same as the student's email");
                }
            }
        }
        checkOverlapCreate(newEvent, newEventStartTime, newEventEndTime, eventList);
        Event event = modelMapper.map(newEvent, Event.class);
        eventRepository.saveAndFlush(event);
        fileStorageService.storeFile(file, event);
        sendEmail(newEvent, "Your appointment is confirmed.");
        return ResponseEntity.status(HttpStatus.OK).body(event).getBody();
    }

    public ResponseEntity<Event> update(EventPutDTO updateEvent, Integer Id, MultipartFile file) throws OverlappedExceptionHandler, HandleExceptionForbidden, IOException {
        Date newEventStartTime = Date.from(updateEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(updateEvent.getEventStartTime()), updateEvent.getEventDuration());
        List<EventDTO> eventList = getEventToCheckOverlap();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        Event eventById = eventRepository.getById(Id);

        String eventDir = eventById.getId().toString();
        Path path = Paths.get(fileStorageProperties.getUploadDir() + "/" + eventDir);

        if (userLogin.getRole().equals(Role.admin)) {
            updateFile(file, path, eventById);
            return checkOverlapUpdate(updateEvent, Id, newEventStartTime, newEventEndTime, eventList);
        } else if (userLogin.getRole().equals(Role.student)) {
            Event eventForCheck = eventRepository.findById(Id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
            );
            if (Objects.equals(updateEvent.getBookingEmail(), userLogin.getEmail())) {
                if (Objects.equals(updateEvent.getBookingEmail(), eventForCheck.getBookingEmail())) {
                    updateFile(file, path, eventById);
                    return checkOverlapUpdate(updateEvent, Id, newEventStartTime, newEventEndTime, eventList);
                } else {
                    throw new HandleExceptionForbidden("You are not owner of this event");
                }
            } else {
                throw new HandleExceptionForbidden("The booking email must be the same as the student's email");
            }
        } else {
            throw new HandleExceptionForbidden("You are not allowed to update this event");
        }
    }

    public void updateFile(MultipartFile file, Path path, Event eventById) throws IOException {
        if (file != null) {
            if (Files.exists(path)) {
                if (!Files.list(path).collect(Collectors.toList()).isEmpty()) {
                    fileStorageService.deleteFile(path + "/" + Files.list(path).collect(Collectors.toList()).get(0).getFileName());
                    fileStorageService.storeFile(file, eventById);
                } else {
                    fileStorageService.storeFile(file, eventById);
                }
            }
        } else {
            if (Files.exists(path)) {
                if (!Files.list(path).collect(Collectors.toList()).isEmpty()) {
                    fileStorageService.deleteFile(path + "/" + getFile(path).get("fileName"));
                }
            }
        }
    }

    public Map<String, String> getFile(Path filePath) throws IOException {
        Map<String, String> fileMap = new HashMap<>();
        if (Files.list(filePath).collect(Collectors.toList()).isEmpty()) {
            fileMap.put("fileName", "");
            fileMap.put("pathFile", filePath.toString());
            return fileMap;
        }
        Path pathFile = Files.list(filePath).collect(Collectors.toList()).get(0);
        System.out.println("PathFile: " + pathFile);
        String fileName = fileStorageService.loadFileAsResource(pathFile.toString()).getFilename();
        System.out.println("FileName: " + fileName);
        fileMap.put("fileName", fileName);
        fileMap.put("pathFile", pathFile.toString());
        return fileMap;
    }


    private void sendEmail(EventPostDTO newEvent, String message) {
        int categoryId = Integer.parseInt(newEvent.getEventCategory().getId().toString());
        Category eventCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Category Id: " + categoryId + "Does Not Exist!"));
        LocalDateTime time = LocalDateTime.ofInstant(newEvent.getEventStartTime(), ZoneId.systemDefault());
        String dateTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String subject = "Dear " + newEvent.getBookingName() + ",";
        String body = message + "\n \n" +
                "Name : " + newEvent.getBookingName() + "\n" +
                "Clinic : " + eventCategory.getEventCategoryName() + "\n" +
                "Date : " + dateTime + "\n" +
                "Duration :" + newEvent.getEventDuration() + " minute \n" +
                "Note : " + newEvent.getEventNotes() + "\n \n" + "-- \n" +
                "Thank you for using our service. \n" +
                "OASIP-SY6 (Admin)";
        emailService.sendEmail(newEvent.getBookingEmail(), subject, body);
    }

    private void checkOverlapCreate(EventPostDTO newEvent, Date newEventStartTime, Date newEventEndTime, List<EventDTO> eventList) throws OverlappedExceptionHandler {
        for (EventDTO eventDTO : eventList) {
            if (Objects.equals(newEvent.getEventCategory().getId(), eventDTO.getEventCategory().getId())) { //เช็คเฉพาะ EventCategory เดียวกัน
                Date eventStartTime = Date.from(eventDTO.getEventStartTime());
                Date eventEndTime = findEndDate(Date.from(eventDTO.getEventStartTime()), eventDTO.getEventDuration());
                checkIfTwoDateRanges(newEventStartTime, newEventEndTime, eventStartTime, eventEndTime);
            }
        }
    }

    private ResponseEntity<Event> checkOverlapUpdate(@RequestBody @Valid EventPutDTO updateEvent, @PathVariable Integer Id, Date newEventStartTime, Date newEventEndTime, List<EventDTO> eventList) throws OverlappedExceptionHandler {
        for (EventDTO eventDTO : eventList) {
            if (Objects.equals(updateEvent.getEventCategory().getId(), eventDTO.getEventCategory().getId()) && !Objects.equals(eventDTO.getId(), Id)) { //เช็คเฉพาะ EventCategory เดียวกัน และถ้าอัพเดตตัวเดิมไม่ต้องเช็ค overlapped
                Date eventStartTime = Date.from(eventDTO.getEventStartTime());
                Date eventEndTime = findEndDate(Date.from(eventDTO.getEventStartTime()), eventDTO.getEventDuration());
                EventService.checkTimeOverlap(newEventStartTime, newEventEndTime, eventStartTime, eventEndTime);
            }
        }
        Event event = eventRepository.findById(Id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
        modelMapper.map(updateEvent, event);
        eventRepository.saveAndFlush(event);
        return ResponseEntity.status(200).body(event);
    }

    public static void checkIfTwoDateRanges(Date newEventStartTime, Date newEventEndTime, Date eventStartTime, Date eventEndTime) throws OverlappedExceptionHandler {
        checkTimeOverlap(newEventStartTime, newEventEndTime, eventStartTime, eventEndTime);
    }

    public static void checkTimeOverlap(Date newEventStartTime, Date newEventEndTime, Date eventStartTime, Date eventEndTime) throws OverlappedExceptionHandler {
        if (newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventStartTime) ||
                newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventEndTime) ||
                newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventEndTime) ||
                newEventStartTime.after(eventStartTime) && newEventEndTime.before(eventEndTime) ||
                newEventStartTime.equals(eventStartTime)) {
            throw new OverlappedExceptionHandler("Time is Overlapped");
        }
    }

    public Date findEndDate(Date date, Integer duration) {
        return new Date(date.getTime() + (duration * 60000 + 60000));
    }

    public List<EventDTO> getEventByCategoryId(Category eventCategoryId) throws HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            List<Event> eventByCategory = eventRepository.findAllByEventCategoryOrderByEventCategoryDesc(eventCategoryId);
            return listMapper.mapList(eventByCategory, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.student)) {
            List<Event> eventByCategory = eventRepository.
                    findAllByBookingEmailAndEventCategoryOrderByEventCategoryDesc(auth.getPrincipal().toString(), eventCategoryId);
            return listMapper.mapList(eventByCategory, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Integer> eventByCategory = userLogin.getEventCategories().stream().map(Category::getId).collect(Collectors.toList());
            if (eventByCategory.contains(eventCategoryId.getId())) {
                List<Event> eventByCategoryId = eventRepository.findAllByEventCategoryOrderByEventCategoryDesc(eventCategoryId);
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
            List<Event> pastEvent = eventRepository.findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.student)) {
            List<Event> pastEvent =
                    eventRepository.findAllByBookingEmailAndEventStartTimeBeforeOrderByEventStartTimeDesc(auth.getPrincipal().toString(), instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Integer> eventByCategory = userLogin.getEventCategories().stream().map(Category::getId).collect(Collectors.toList());
            List<Event> pastEvent = eventRepository.findAllByEventCategory_IdInAndEventStartTimeBeforeOrderByEventStartTimeDesc(eventByCategory, instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userLogin.getEmail() + "is not owner of this event");
        }
    }

    public List<EventDTO> getUpcomingEvent(Instant instant) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            List<Event> pastEvent = eventRepository.findAllByEventStartTimeAfterOrderByEventStartTimeAsc(instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.student)) {
            List<Event> pastEvent = eventRepository.
                    findAllByBookingEmailAndEventStartTimeAfterOrderByEventStartTimeAsc(auth.getPrincipal().toString(), instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Integer> eventByCategory = userLogin.getEventCategories().stream().map(Category::getId).collect(Collectors.toList());
            List<Event> pastEvent = eventRepository.findAllByEventCategory_IdInAndEventStartTimeAfterOrderByEventStartTimeAsc(eventByCategory, instant);
            return listMapper.mapList(pastEvent, EventDTO.class, modelMapper);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userLogin.getEmail() + "is not owner of this event");
        }
    }

    public List<EventDTO> getEventByDateTime(String startTime, String endTime) throws HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            List<Event> eventByDateTime = eventRepository.findAllByEventStartTimeBetween(Instant.parse(startTime), Instant.parse(endTime));
            return listMapper.mapList(eventByDateTime, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.student)) {
            List<Event> eventByDateTime = eventRepository.
                    findAllByBookingEmailAndEventStartTimeBetween(auth.getPrincipal().toString(), Instant.parse(startTime), Instant.parse(endTime));
            return listMapper.mapList(eventByDateTime, EventDTO.class, modelMapper);
        } else if (userLogin.getRole().equals(Role.lecturer)) {
            List<Event> eventList = eventRepository.findByEventCategory_IdInAndEventStartTimeBetween(userLogin.getEventCategories().stream().map(Category::getId).collect(Collectors.toList()),
                    Instant.parse(startTime), Instant.parse(endTime));
            if (eventList != null) {
                return listMapper.mapList(eventList, EventDTO.class, modelMapper);
            } else {
                throw new HandleExceptionForbidden("You are not category owner of this event");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userLogin.getEmail() + "is not owner of this event");
        }
    }
}


