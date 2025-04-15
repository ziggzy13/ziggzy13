package models;

/**
 * Book model class
 */
public class Book {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private String availability;
    private String addedAt;
    
    /**
     * Default constructor
     */
    public Book() {
    }
    
    /**
     * Constructor with parameters
     * @param bookId Book ID
     * @param title Book title
     * @param author Book author
     * @param genre Book genre
     * @param availability Book availability status
     * @param addedAt Book addition date
     */
    public Book(int bookId, String title, String author, String genre, String availability, String addedAt) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availability = availability;
        this.addedAt = addedAt;
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
     * Get book title
     * @return Book title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Set book title
     * @param title Book title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Get book author
     * @return Book author
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * Set book author
     * @param author Book author
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    
    /**
     * Get book genre
     * @return Book genre
     */
    public String getGenre() {
        return genre;
    }
    
    /**
     * Set book genre
     * @param genre Book genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    /**
     * Get book availability
     * @return Book availability
     */
    public String getAvailability() {
        return availability;
    }
    
    /**
     * Set book availability
     * @param availability Book availability
     */
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    
    /**
     * Get book addition date
     * @return Book addition date
     */
    public String getAddedAt() {
        return addedAt;
    }
    
    /**
     * Set book addition date
     * @param addedAt Book addition date
     */
    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }
    
    /**
     * Check if book is available
     * @return true if book is available, false otherwise
     */
    public boolean isAvailable() {
        return "налична".equals(availability);
    }
    
    /**
     * String representation of the book
     * @return String representation
     */
    @Override
    public String toString() {
        return title + " - " + author;
    }
}