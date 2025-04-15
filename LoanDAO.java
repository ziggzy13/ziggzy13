package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Loan;
import utils.DBUtil;

/**
 * Data Access Object for Loan entity
 */
public class LoanDAO {
    
    /**
     * Create a new loan
     * @param bookId Book ID
     * @param userId User ID
     * @return true if loan created successfully, false otherwise
     */
    public boolean createLoan(int bookId, int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // Start transaction
            conn.setAutoCommit(false);
            
            // Update book availability
            String updateBookSql = "UPDATE books SET availability = 'заета' WHERE book_id = ?";
            stmt = conn.prepareStatement(updateBookSql);
            stmt.setInt(1, bookId);
            int bookUpdated = stmt.executeUpdate();
            
            if (bookUpdated > 0) {
                // Create loan record
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date();
                String loanDate = sdf.format(currentDate);
                
                // Calculate return date (14 days from now)
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);
                cal.add(Calendar.DATE, 14);
                String returnDate = sdf.format(cal.getTime());
                
                String createLoanSql = "INSERT INTO loans (book_id, user_id, loan_date, return_date) VALUES (?, ?, ?, ?)";
                stmt.close();
                stmt = conn.prepareStatement(createLoanSql);
                stmt.setInt(1, bookId);
                stmt.setInt(2, userId);
                stmt.setString(3, loanDate);
                stmt.setString(4, returnDate);
                
                int loanCreated = stmt.executeUpdate();
                
                if (loanCreated > 0) {
                    conn.commit();
                    success = true;
                } else {
                    conn.rollback();
                }
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBUtil.close(stmt, null);
        }
        
        return success;
    }
    
    /**
     * Return a book
     * @param loanId Loan ID
     * @return true if book returned successfully, false otherwise
     */
    public boolean returnBook(int loanId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;
        
        try {
            conn = DBUtil.getConnection();
            
            // Start transaction
            conn.setAutoCommit(false);
            
            // Get book ID from loan
            String getLoanSql = "SELECT book_id FROM loans WHERE loan_id = ?";
            stmt = conn.prepareStatement(getLoanSql);
            stmt.setInt(1, loanId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                
                // Update book availability
                String updateBookSql = "UPDATE books SET availability = 'налична' WHERE book_id = ?";
                stmt.close();
                stmt = conn.prepareStatement(updateBookSql);
                stmt.setInt(1, bookId);
                int bookUpdated = stmt.executeUpdate();
                
                if (bookUpdated > 0) {
                    // Update loan record
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String returnDate = sdf.format(new Date());
                    
                    String updateLoanSql = "UPDATE loans SET actual_return_date = ? WHERE loan_id = ?";
                    stmt.close();
                    stmt = conn.prepareStatement(updateLoanSql);
                    stmt.setString(1, returnDate);
                    stmt.setInt(2, loanId);
                    
                    int loanUpdated = stmt.executeUpdate();
                    
                    if (loanUpdated > 0) {
                        conn.commit();
                        success = true;
                    } else {
                        conn.rollback();
                    }
                } else {
                    conn.rollback();
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBUtil.close(stmt, rs);
        }
        
        return success;
    }
    
    /**
     * Get loans by user ID
     * @param userId User ID
     * @param activeOnly Get only active loans
     * @return List of loans
     */
    public List<Loan> getLoansByUserId(int userId, boolean activeOnly) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Loan> loans = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            
            String sql = "SELECT l.*, b.title, b.author FROM loans l " +
                         "JOIN books b ON l.book_id = b.book_id " +
                         "WHERE l.user_id = ?";
            
            if (activeOnly) {
                sql += " AND l.actual_return_date IS NULL";
            }
            
            sql += " ORDER BY l.loan_date DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setLoanDate(rs.getString("loan_date"));
                loan.setReturnDate(rs.getString("return_date"));
                loan.setActualReturnDate(rs.getString("actual_return_date"));
                loan.setBookTitle(rs.getString("title"));
                loan.setBookAuthor(rs.getString("author"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, rs);
        }
        
        return loans;
    }
    
    /**
     * Get all loans
     * @param activeOnly Get only active loans
     * @return List of loans
     */
    public List<Loan> getAllLoans(boolean activeOnly) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Loan> loans = new ArrayList<>();
        
        try {
            conn = DBUtil.getConnection();
            
            String sql = "SELECT l.*, b.title, b.author, u.name FROM loans l " +
                         "JOIN books b ON l.book_id = b.book_id " +
                         "JOIN users u ON l.user_id = u.user_id";
            
            if (activeOnly) {
                sql += " WHERE l.actual_return_date IS NULL";
            }
            
            sql += " ORDER BY l.loan_date DESC";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setLoanDate(rs.getString("loan_date"));
                loan.setReturnDate(rs.getString("return_date"));
                loan.setActualReturnDate(rs.getString("actual_return_date"));
                loan.setBookTitle(rs.getString("title"));
                loan.setBookAuthor(rs.getString("author"));
                loan.setUserName(rs.getString("name"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, rs);
        }
        
        return loans;
    }
    
    /**
     * Get loan by ID
     * @param loanId Loan ID
     * @return Loan object or null if not found
     */
    public Loan getLoanById(int loanId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Loan loan = null;
        
        try {
            conn = DBUtil.getConnection();
            
            String sql = "SELECT l.*, b.title, b.author, u.name FROM loans l " +
                         "JOIN books b ON l.book_id = b.book_id " +
                         "JOIN users u ON l.user_id = u.user_id " +
                         "WHERE l.loan_id = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, loanId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setLoanDate(rs.getString("loan_date"));
                loan.setReturnDate(rs.getString("return_date"));
                loan.setActualReturnDate(rs.getString("actual_return_date"));
                loan.setBookTitle(rs.getString("title"));
                loan.setBookAuthor(rs.getString("author"));
                loan.setUserName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt, rs);
        }
        
        return loan;
    }
}