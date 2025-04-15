package utils;

import models.User;

/**
 * Session manager for tracking logged in user
 */
public class SessionManager {
    private static User currentUser;
    
    /**
     * Get current logged in user
     * @return User object or null if not logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Set current logged in user
     * @param user User object
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    /**
     * Check if user is logged in
     * @return true if user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Check if current user is administrator
     * @return true if current user is administrator, false otherwise
     */
    public static boolean isAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }
    
    /**
     * Logout current user
     */
    public static void logout() {
        currentUser = null;
    }
}