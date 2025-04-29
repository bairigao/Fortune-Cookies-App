import com.example.fortune_cookies_app.model.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthValidatorTest {

    private AuthValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new AuthValidator();
    }

    // --- Signup Tests ---

    @Test
    public void passwordsDoNotMatch() {
        assertFalse(validator.isPasswordConfirmed("abc123", "xyz456"));
    }

    @Test
    public void passwordsMatch() {
        assertTrue(validator.isPasswordConfirmed("abc123", "abc123"));
    }

    @Test
    public void requiredFieldIsEmpty() {
        assertFalse(validator.areSignupFieldsValid("John", "", "john@example.com", "abc123", "abc123"));
    }

    @Test
    public void allFieldsAreValid() {
        assertTrue(validator.areSignupFieldsValid("John", "Doe", "john@example.com", "abc123", "abc123"));
    }

    @Test
    public void emailIsInvalid() {
        assertFalse(validator.isValidEmail("john@invalid@com"));
    }

    @Test
    public void emailIsValid() {
        assertTrue(validator.isValidEmail("john.doe@example.com"));
    }

    // --- Login Tests ---

    @Test
    public void emailOrPasswordIsEmpty() {
        assertFalse(validator.areLoginFieldsValid("", "password"));
        assertFalse(validator.areLoginFieldsValid("email@example.com", ""));
    }

    @Test
    public void emailAndPasswordProvided() {
        assertTrue(validator.areLoginFieldsValid("user@example.com", "password123"));
    }
}
