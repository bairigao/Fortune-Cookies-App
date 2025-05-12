import com.example.fortune_cookies_app.model.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthValidatorTest {


    @BeforeEach
    public void setUp() {
        new AuthValidator();
    }

    // --- Signup Tests ---

    @Test
    public void passwordsDoNotMatch() {
        assertFalse(AuthValidator.isPasswordConfirmed("abc123", "xyz456"));
    }

    @Test
    public void passwordsMatch() {
        assertTrue(AuthValidator.isPasswordConfirmed("abc123", "abc123"));
    }

    @Test
    public void requiredFieldIsEmpty() {
        assertFalse(AuthValidator.areSignupFieldsValid("John", "", "john@example.com", "abc123", "abc123"));
    }

    @Test
    public void allFieldsAreValid() {
        assertTrue(AuthValidator.areSignupFieldsValid("John", "Doe", "john@example.com", "abc123", "abc123"));
    }

    @Test
    public void emailIsInvalid() {
        assertFalse(AuthValidator.isValidEmail("john@invalid@com"));
    }

    @Test
    public void emailIsValid() {
        assertTrue(AuthValidator.isValidEmail("john.doe@example.com"));
    }

    // --- Login Tests ---

    @Test
    public void emailOrPasswordIsEmpty() {
        assertFalse(AuthValidator.areLoginFieldsValid("", "password"));
        assertFalse(AuthValidator.areLoginFieldsValid("email@example.com", ""));
    }

    @Test
    public void emailAndPasswordProvided() {
        assertTrue(AuthValidator.areLoginFieldsValid("user@example.com", "password123"));
    }

    @Test
    public void emailFormat() {
        String emailAddress = "user@example.com";
        assertTrue(AuthValidator.isValidEmail(emailAddress));
    }

    @Test
    public void passwordFormat() {
        String password = "Password@123";
        assertTrue(AuthValidator.isStrongPassword(password));
    }

//    @Test
//    public void testUpdatePassword_InAppChange_Success() {
//        boolean result = updatePassword("user@example.com", "oldPwd123", "newPwd123", null);
//        assertTrue(result);
//    }
//
//    @Test
//    public void testUpdatePassword_Recovery_Success() {
//        boolean result = userDAO.updatePassword("user@example.com", null, "newPwd123", "myPetName");
//        assertTrue(result);
//    }
//
//    @Test
//    public void testUpdatePassword_InAppChange_WrongPassword() {
//        boolean result = userDAO.updatePassword("user@example.com", "wrongOldPwd", "newPwd123", null);
//        assertFalse(result);
//    }
//
//    @Test
//    public void testUpdatePassword_Recovery_WrongAnswer() {
//        boolean result = userDAO.updatePassword("user@example.com", null, "newPwd123", "wrongAnswer");
//        assertFalse(result);
//    }

}
