import static org.junit.jupiter.api.Assertions.*;
import com.example.fortune_cookies_app.model.EventDAO;
import com.example.fortune_cookies_app.model.User;
import org.junit.jupiter.api.*;
import com.example.fortune_cookies_app.model.Event;

import java.sql.SQLException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventTest{
    private EventDAO eventDAO;;
    private User user;
    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = null;
    private static final Event[] events = {
            new Event(LocalDate.of(2023, 10, 1), "Birthday", "My birthday party", 5, 1),
            new Event(LocalDate.of(2023, 10, 2), "Meeting", "Project meeting", 3, 2),
            new Event(LocalDate.of(2023, 10, 3), "Doctor's appointment", "Check-up", 4, 3),
            new Event(LocalDate.of(2023, 10, 4), "Anniversary", "Wedding anniversary", 5, 4),
            new Event(LocalDate.of(2023, 10, 5), "Conference", "Tech conference", 4, 5)
    };


    List<Event> matches;

    @BeforeAll
    public void setUp(){
        eventDAO = new EventDAO();
        user = new User("John", "Doe", "johndoes@example.com", "password123", "What is your pet's name?", "niuyou", 1);
        user.setId(100);
        events[0] = new Event(LocalDate.now(), "TestEvent", "EventDescription", 3, user.getId());
        matches = new ArrayList<>();
        for (Event event : events) {
            eventDAO.createEvent(event);
        }
    }

    @AfterEach
    public void tearDown() {
        matches.clear();
        eventDAO.clearEvents(user.getId());
    }
    @AfterAll
    public void tearDown2(){
        for (int i = 1; i < 6; i++){
            eventDAO.clearEvents(i);
        }
    }

    @Test
    public void testFetchEvents(){
        user.setId(2);
        matches = eventDAO.fetchEvents(user);

    }

    @Test
    public void testFetchSingleEvent() throws SQLException {
        Event newEvent = eventDAO.fetchSingleEvent(events[0].getId());
    }

    @Test
    public void testCreateEvent(){
        assertTrue(events[0].getId() > 0);
    }


    @Test
    public void testUpdateEvent() throws SQLException {
        Event event = new Event(LocalDate.now(), "TestEvent", "TestDescription", 1, user.getId());
        eventDAO.createEvent(event);
        String originalName = event.getEventName();
        event.setEventName("NewEventName");
        System.out.println(event.getEventName());
        eventDAO.updateEvent(event);

        Event newEvent = eventDAO.fetchSingleEvent(event.getId());
        System.out.println(newEvent.getEventName());
        assertNotEquals(originalName, newEvent.getEventName());
    }

    @Test
    public void testFetchEventsDay(){
        Event event1 = new Event(LocalDate.now(), "Test1", "TestDescription", 3, user.getId());
        Event event2 = new Event(LocalDate.now(), "Test1", "TestDescription", 3, user.getId());
        Event event3 = new Event(LocalDate.now(), "Test1", "TestDescription", 3, user.getId());
        Event event4 = new Event(LocalDate.now().minusDays(1), "Test1", "TestDescription", 3, user.getId());

        List<Event> testEvents = eventDAO.fetchEventsDay(user, LocalDate.now());

        for (Event testEvent : testEvents) {
            assertEquals(LocalDate.now(), testEvent.getDate());
        }
    }
    @Test
    public void testDeleteEvent(){
        for (Event event : events) {
            eventDAO.deleteEvent(event);
        }
        List<Event> currentEvents = eventDAO.fetchEvents(user);
        assertTrue(currentEvents.isEmpty());

        setUp();
    }

    @Test
    public void testEventTitleTooLong() {
        String longTitle = "A".repeat(51);
        Event event = new Event(LocalDate.now(), longTitle, "desc", 3, 1);
        assertTrue(event.getEventName().length() > 50, "Expected title to exceed 50 characters");
    }

    @Test
    public void testMissingTitle() {
        Event event = new Event(LocalDate.now(), "", "desc", 3, 1);
        assertTrue(event.getEventName().isEmpty(), "Title is blank as expected");
    }
}
