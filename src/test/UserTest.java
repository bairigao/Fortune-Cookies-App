import static org.junit.jupiter.api.Assertions.*;
import com.example.fortune_cookies_app.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private static final String FIRST_NAME = "John";
    private static final String FIRST_NAME_TWO = "Jane";
    private static final String LAST_NAME = "Doe";
    private static final String LAST_NAME_TWO = "Doe";
    private static final String EMAIL = "john@gmail.com";
    private static final String EMAIL_TWO = "jane@gmail.com";
    private static final String PASSWORD = "password123";
    private static final String PASSWORD_TWO = "password1234";
    private static final int LOGINSTREAK = '3';
    private static final int LOGINSTREAK_TWO = '4';

    private User user;
    private User userTwo;

    @BeforeEach
    public void setUp() {
        user = new User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, LOGINSTREAK);
        userTwo = new User(FIRST_NAME_TWO, LAST_NAME_TWO, EMAIL_TWO, PASSWORD_TWO, LOGINSTREAK_TWO);
    }
    @Test
    public void testSetId() {
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    public void testGetFirstName() {
        assertEquals(FIRST_NAME, user.getFirstName());
    }

    @Test
    public void testSetFirstName() {
        user.setFirstName(FIRST_NAME_TWO);
        assertEquals(FIRST_NAME_TWO, user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals(LAST_NAME, user.getLastName());
    }

    @Test
    public void testSetLastName() {
        user.setLastName(LAST_NAME_TWO);
        assertEquals(LAST_NAME_TWO, user.getLastName());
    }

    @Test
    public void testGetEmail() {
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void testSetEmail() {
        user.setEmail(EMAIL_TWO);
        assertEquals(EMAIL_TWO, user.getEmail());
    }

    @Test
    public void testGetPassword() {
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword(PASSWORD_TWO);
        assertEquals(PASSWORD_TWO, user.getPassword());
    }

    // test if the email contains an @ symbol
    @Test
    public void testEmailContainsAt() {
        assertTrue(EMAIL.contains("@"));
    }

    // test if the email contains a . symbol
    @Test
    public void testEmailContainsDot() {
        assertTrue(EMAIL.contains("."));
    }

    // test if the password is not null
    @Test
    public void testPasswordIsNotNull() {
        assertNotNull(PASSWORD);
    }

    // test if the email is not null
    @Test
    public void testEmailIsNotNull() {
        assertNotNull(EMAIL);
    }


}