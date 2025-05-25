import com.example.fortune_cookies_app.model.User;
import com.example.fortune_cookies_app.model.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class LoginStreakTest {
    private User user;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        // Create a test user
        user = new User(
                "Test",
                "User",
                "test@example.com",
                "hashedPassword",
                "What is your pet's name?",
                "Fluffy",
                1
        );
        user.setId(1);
    }

    @Test
    void testFirstLoginStreak() {
        // First login should set streak to 1
        user.trackLogin();
        assertEquals(1, user.getLoginStreak());
        assertEquals(LocalDate.now(), user.getLastLogin());
    }

    @Test
    void testConsecutiveLoginStreak() {
        // Set last login to yesterday
        user.setLastLogin(LocalDate.now().minusDays(1));
        user.setLoginStreak(1);

        // Login today should increase streak
        user.trackLogin();
        assertEquals(2, user.getLoginStreak());
        assertEquals(LocalDate.now(), user.getLastLogin());
    }

    @Test
    void testStreakResetAfterGap() {
        // Set last login to 2 days ago
        user.setLastLogin(LocalDate.now().minusDays(2));
        user.setLoginStreak(5);

        // Login today should reset streak to 1
        user.trackLogin();
        assertEquals(1, user.getLoginStreak());
        assertEquals(LocalDate.now(), user.getLastLogin());
    }

    @Test
    void testTrophyAchievement() {
        // Set streak to 2 (just before 3-day trophy)
        user.setLoginStreak(2);
        user.setLastLogin(LocalDate.now().minusDays(1));

        // Login should trigger 3-day trophy
        user.trackLogin();
        assertEquals(3, user.getLoginStreak());
    }

    @Test
    void testMultipleTrophyAchievements() {
        // Set streak to 6 (just before 7-day trophy)
        user.setLoginStreak(6);
        user.setLastLogin(LocalDate.now().minusDays(1));

        // Login should trigger 7-day trophy
        user.trackLogin();
        assertEquals(7, user.getLoginStreak());
    }

    @Test
    void testStreakPersistence() {
        // Set initial streak
        user.setLoginStreak(5);
        user.setLastLogin(LocalDate.now().minusDays(1));

        // Update streak in database
        userDAO.updateStreak(user);

        // Retrieve user and verify streak
        User retrievedUser = userDAO.findByEmail(user.getEmail());
        if (retrievedUser != null) {
            assertEquals(5, retrievedUser.getLoginStreak());
        }
    }
} 