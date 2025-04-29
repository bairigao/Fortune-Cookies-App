import com.example.fortune_cookies_app.model.EventDAO;
import com.example.fortune_cookies_app.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import com.example.fortune_cookies_app.model.Event;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventTest{
    private EventDAO eventDAO;
    private Event event;
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
        matches = new ArrayList<>();
        for (Event event : events) {
            eventDAO.createEvent(event);
        }
    }

    @AfterEach
    public void tearDown() {
        matches.clear();
    }

    @Test
    public void testFetchEvents(){
        User user = new User("John", "Doe", "johndoes@example.com", "password123", '3');
        user.setId(2);
        matches = eventDAO.fetchEvents(user);

    }

}
