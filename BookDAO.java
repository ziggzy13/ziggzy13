package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Book;
import utils.DBUtil;

/**
 * Data Access Object for Book entity
 */
public class BookDAO {
    /**
     * Get book by ID
     * @param bookId Book ID
     * @return Book object or null if not found
     */
    public Book getBookById(int bookId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Book book = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM books WHERE book_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setAvailability(rs.getString("availability"));
                book.setAddedAt(rs.getString("added_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, rs);
        }
        
        return book;
    }
    
    /**
     * Get all books
     * @return List of books
     */
    public List<Book> getAllBooks() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM books ORDER BY title";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setAvailability(rs.getString("availability"));
                book.setAddedAt(rs.getString("added_at"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, rs);
        }
        
        return books;
    }
    
    /**
     * Search books by title, author or genre
     * @param searchType Type of search (title, author, genre)
     * @param searchTerm Search term
     * @return List of books matching search criteria
     */
    public List<Book> searchBooks(String searchType, String searchTerm) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            
            String sql = "SELECT * FROM books WHERE " + searchType + " LIKE ? ORDER BY title";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setAvailability(rs.getString("availability"));
                book.setAddedAt(rs.getString("added_at"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, rs);
        }
        
        return books;
    }
    
    /**
     * Add new book
     * @param book Book object
     * @return true if book added successfully, false otherwise
     */
    public boolean addBook(Book book) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO books (title, author, genre, availability) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setString(4, book.getAvailability() != null ? book.getAvailability() : "налична");
            
            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, null);
        }
        
        return success;
    }
    
    /**
     * Update book
     * @param book Book object
     * @return true if book updated successfully, false otherwise
     */
    public boolean updateBook(Book book) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE books SET title = ?, author = ?, genre = ?, availability = ? WHERE book_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setString(4, book.getAvailability());
            stmt.setInt(5, book.getBookId());
            
            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, null);
        }
        
        return success;
    }
    
    /**
     * Delete book
     * @param bookId Book ID
     * @return true if book deleted successfully, false otherwise
     */
    public boolean deleteBook(int bookId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM books WHERE book_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            
            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, null);
        }
        
        return success;
    }
    
    /**
     * Update book availability
     * @param bookId Book ID
     * @param availability New availability status
     * @return true if availability updated successfully, false otherwise
     */
    public boolean updateBookAvailability(int bookId, String availability) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE books SET availability = ? WHERE book_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, availability);
            stmt.setInt(2, bookId);
            
            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, null);
        }
        
        return success;
    }
}