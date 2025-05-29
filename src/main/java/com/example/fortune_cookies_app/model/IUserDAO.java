package com.example.fortune_cookies_app.model;

/**
 * Interface to define the user class
 */
public interface IUserDAO {
    /**
     * Creates a new user in the system.
     *
     * @param user the user object containing details such as name, email, password,
     *             and other relevant information to be persisted
     */
    void createUser(User user);

    /**
     * Authenticates a user by verifying the provided username and password credentials.
     *
     * @param userName the username of the user attempting to log in
     * @param password the password associated with the user's account
     * @return the User object corresponding to the authenticated user,
     *         or null if the authentication fails
     */
    User login(String userName, String password);

    /**
     * Updates the user's password if the current password is verified.
     *
     * @param user the user object whose password needs to be updated
     * @param currentPassword the current password provided for verification
     * @param newPassword the new password to be set for the user
     */
    void updatePassword(User user, String currentPassword, String newPassword);

    /**
     * Updates the login streak of the given user. The method modifies the user's
     * login streak data by evaluating criteria such as consecutive days of login.
     *
     * @param user the user whose login streak is to be updated; the user object
     *             must include relevant fields such as lastLogin and loginStreak
     */
    void updateStreak(User user);

    /**
     * Resets the password for the specified user, replacing it with the provided new password.
     * This method does not require the user's current password for verification.
     *
     * @param user the user whose password needs to be reset; the user object must reference a valid user
     * @param newPassword the new password to be set for the user account
     */
    void resetPassword(User user, String newPassword);
    /**
     * Checks if the provided email exists in the system.
     *
     * @param email the email address to be checked
     * @return true if the email exists, false otherwise
     */
    boolean checkEmail(String email);

    /**
     * Finds and retrieves a User object based on the provided email address.
     *
     * @param email the email address of the user to be retrieved
     * @return the User object associated with the provided email,
     *         or null if no such user exists
     */
    User findByEmail(String email);

}
