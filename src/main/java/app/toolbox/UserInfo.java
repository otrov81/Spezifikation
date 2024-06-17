package app.toolbox;

import app.form.login.LoginForm;
import app.model.User;
import app.repository.UserRepository;
import app.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Utility class to manage user information and roles.
 */
public class UserInfo {

    @Autowired
    private DBService dbService;

    private String userInfo;
    private LoginForm loginForm;
    private String userName;
    private String adminRolle;
    private UserRepository userRepository;

    /**
     * Constructor to initialize UserInfo with DBService dependency.
     *
     * @param dbService DBService instance for database operations
     */
    public UserInfo(DBService dbService) {
        this.dbService = dbService;
    }

    /**
     * Retrieves the role (admin or user) based on user information.
     *
     * @return String representing user role ("admin" or "user")
     */
    public String getUserRolle() {
        String adminRolle = getAdminRolle(); // Get admin role status
        String username = loginForm.getTxtUsername(); // Get username from login form

        if (username != null && !username.isEmpty()) { // Check if username is valid
            User berechtigung = dbService.findByUsername(username); // Retrieve user details
            if (berechtigung != null && berechtigung.getBerechtigung() != null) {
                // Check user permissions
                if (berechtigung.getBerechtigung().equals("1")) {
                    userInfo = "admin";
                } else {
                    userInfo = "user";
                }
            } else {
                userInfo = "defaultUserInfo"; // Handle default user info state
            }
        } else if ("1".equals(adminRolle)) { // Check if user has admin role
            userInfo = "admin";
        } else {
            userInfo = "user"; // Default to user role
        }
        return userInfo;
    }

    /**
     * Retrieves the admin role status from the database.
     *
     * @return String representing admin role ("1" for admin, default value otherwise)
     */
    public String getAdminRolle() {
        User berechtigung = dbService.findByUsername(loginForm.getTxtUsername()); // Get user details
        if (berechtigung != null) {
            adminRolle = berechtigung.getBerechtigung(); // Retrieve admin role status
        } else {
            adminRolle = "defaultRole"; // Handle default role when user details are not found
        }
        return adminRolle;
    }

    /**
     * Retrieves the formatted username for display.
     *
     * @return String representing formatted username
     */
    public String getUserName() {
        String username = loginForm.getTxtUsername(); // Get username from login form

        if (username != null && !username.isEmpty()) { // Check if username is valid
            userName = LoginForm.getTxtUsername(); // Get username from login form
            User berechtigung = dbService.findByUsername(LoginForm.getTxtUsername()); // Retrieve user details
        } else if (getAdminRolle().equals("1")) { // Check if user has admin role
            userName = System.getProperty("user.name"); // Use system username
        } else {
            userName = System.getProperty("user.name"); // Use system username
        }

        // Capitalize first letter of username
        String output = userName.substring(0, 1).toUpperCase() + userName.substring(1);
        return output;
    }
}
