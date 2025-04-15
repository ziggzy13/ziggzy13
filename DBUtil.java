package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for database operations
 */
public class DBUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_system?useUnicode=true&characterEncoding=utf8";
    private static final String DB_USER = "root"; // Change to your database username
    private static final String DB_PASSWORD = "KTN_ClanBG123!!!"; // Change to your database password
    
    private static Connection connection;
    
    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Create connection
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add the required library to your project.");
            }
        }
        return connection;
    }
    
    /**
     * Close database connection and resources
     * @param connection Connection object
     * @param statement PreparedStatement object
     * @param resultSet ResultSet object
     */
    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            
            if (statement != null) {
                statement.close();
            }
            
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Close PreparedStatement and ResultSet
     * @param statement PreparedStatement object
     * @param resultSet ResultSet object
     */
    public static void close(PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}