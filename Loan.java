package models;

/**
 * Loan model class
 */
public class Loan {
    private int loanId;
    private int bookId;
    private int userId;
    private String loanDate;
    private String returnDate;
    private String actualReturnDate;
    
    // Additional fields for display
    private String bookTitle;
    private String bookAuthor;
    private String userName;
    
    /**
     * Default constructor
     */
    public Loan() {
    }
    
    /**
     * Constructor with parameters
     * @param loanId Loan ID
     * @param bookId Book ID
     * @param userId User ID
     * @param loanDate Loan date
     * @param returnDate Expected return date
     * @param actualReturnDate Actual return date
     */
    public Loan(int loanId, int bookId, int userId, String loanDate, String returnDate, String actualReturnDate) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.actualReturnDate = actualReturnDate;
    }
    
    /**
     * Get loan ID
     * @return Loan ID
     */
    public int getLoanId() {
        return loanId;
    }
    
    /**
     * Set loan ID
     * @param loanId Loan ID
     */
    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }
    
    /**
     * Get book ID
     * @return Book ID
     */
    public int getBookId() {
        return bookId;
    }
    
    /**
     * Set book ID
     * @param bookId Book ID
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
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
     * Get loan date
     * @return Loan date
     */
    public String getLoanDate() {
        return loanDate;
    }
    
    /**
     * Set loan date
     * @param loanDate Loan date
     */
    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }
    
    /**
     * Get expected return date
     * @return Expected return date
     */
    public String getReturnDate() {
        return returnDate;
    }
    
    /**
     * Set expected return date
     * @param returnDate Expected return date
     */
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
    
    /**
     * Get actual return date
     * @return Actual return date
     */
    public String getActualReturnDate() {
        return actualReturnDate;
    }
    
    /**
     * Set actual return date
     * @param actualReturnDate Actual return date
     */
    public void setActualReturnDate(String actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }
    
    /**
     * Get book title
     * @return Book title
     */
    public String getBookTitle() {
        return bookTitle;
    }
    
    /**
     * Set book title
     * @param bookTitle Book title
     */
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    /**
     * Get book author
     * @return Book author
     */
    public String getBookAuthor() {
        return bookAuthor;
    }
    
    /**
     * Set book author
     * @param bookAuthor Book author
     */
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    
    /**
     * Get user name
     * @return User name
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Set user name
     * @param userName User name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Check if loan is active (not returned)
     * @return true if loan is active, false otherwise
     */
    public boolean isActive() {
        return actualReturnDate == null || actualReturnDate.isEmpty();
    }
    
    /**
     * String representation of the loan
     * @return String representation
     */
    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", bookTitle='" + bookTitle + '\'' +
                ", userName='" + userName + '\'' +
                ", loanDate='" + loanDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", actualReturnDate='" + actualReturnDate + '\'' +
                '}';
    }
}