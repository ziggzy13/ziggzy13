package models;

/**
 * User model class
 */
public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private String createdAt;
    
    /**
     * Default constructor
     */
    public User() {
    }
    
    /**
     * Constructor with parameters
     * @param userId User ID
     * @param name User name
     * @param email User email
     * @param password User password
     * @param role User role
     * @param createdAt User creation date
     */
    public User(int userId, String name, String email, String password, String role, String createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }
    
    /**
     * Get user ID
     * @return User ID
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Set user ID
     * @param userId User ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    /**
     * Get user name
     * @return User name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set user name
     * @param name User name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get user email
     * @return User email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Set user email
     * @param email User email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Get user password
     * @return User password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Set user password
     * @param password User password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Get user role
     * @return User role
     */
    public String getRole() {
        return role;
    }
    
    /**
     * Set user role
     * @param role User role
     */
    public void setRole(String role) {
        this.role = role;
    }
    
    /**
     * Get user creation date
     * @return User creation date
     */
    public String getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Set user creation date
     * @param createdAt User creation date
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Check if user is administrator
     * @return true if user is administrator, false otherwise
     */
    public boolean isAdmin() {
        return "администратор".equals(role);
    }
    
    /**
     * String representation of the user
     * @return String representation
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}