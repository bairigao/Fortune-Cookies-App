import com.example.fortune_cookies_app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link User} class.
 * This class verifies the correct behavior of the User's getters, setters, and ID management.
 */
public class UserTest {

    private static final String FIRST_NAME = "John";
    private static final String FIRST_NAME_TWO = "Jane";
    private static final String LAST_NAME = "Doe";
    private static final String LAST_NAME_TWO = "Doe";
    private static final String EMAIL = "john@gmail.com";
    private static final String EMAIL_TWO = "jane@gmail.com";
    private static final String PASSWORD = "password123";
    private static final String PASSWORD_TWO = "password1234";
    private static final String SECURITY_QUESTION = "What is your pet's name?";
    private static final String SECURITY_ANSWER = "Mittens";
    private static final int loginStreak = 3;

    private User user;


    /**
     * Sets up fresh User instances before each test is run.
     */
    @BeforeEach
    public void setUp() {
        user = new User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, SECURITY_QUESTION, SECURITY_ANSWER, loginStreak);
    }


    /**
     * Tests setting and getting the user's ID.
     */
    @Test
    public void testSetId() {
        user.setId(1);
        assertEquals(1, user.getId());
    }

    /**
     * Tests retrieving the user's first name.
     */
    @Test
    public void testGetFirstName() {
        assertEquals(FIRST_NAME, user.getFirstName());
    }

    /**
     * Tests setting the user's first name.
     */
    @Test
    public void testSetFirstName() {
        user.setFirstName(FIRST_NAME_TWO);
        assertEquals(FIRST_NAME_TWO, user.getFirstName());
    }

    /**
     * Tests retrieving the user's last name.
     */
    @Test
    public void testGetLastName() {
        assertEquals(LAST_NAME, user.getLastName());

    }

    /**
     * Tests setting the user's last name.
     */
    @Test
    public void testSetLastName() {
        user.setLastName(LAST_NAME_TWO);
        assertEquals(LAST_NAME_TWO, user.getLastName());
    }

    /**
     * Tests retrieving the user's email address.
     */
    @Test
    public void testGetEmail() {
        assertEquals(EMAIL, user.getEmail());
    }

    /**
     * Tests setting the user's email address.
     */
    @Test
    public void testSetEmail() {
        user.setEmail(EMAIL_TWO);
        assertEquals(EMAIL_TWO, user.getEmail());
    }


    /**
     * Tests retrieving the user's password.
     */
    @Test
    public void testGetPassword() {
        assertEquals(PASSWORD, user.getPassword());
    }

    /**
     * Tests setting the user's password.
     */
    @Test
    public void testSetPassword() {
        user.setPassword(PASSWORD_TWO);
        assertEquals(PASSWORD_TWO, user.getPassword());
    }

    // test if the email contains an @ symbol
    @Test
    public void testEmailContainsAt() {
        String notAnEmail = "emailaddress";
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