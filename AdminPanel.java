package ui;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import dao.LoanDAO;
import dao.UserDAO;
import models.Book;
import models.Loan;
import models.User;
import utils.SessionManager;

/**
 * Admin panel for administrative functions
 */
public class AdminPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    // Books tab
    private JTable booksTable;
    private DefaultTableModel booksModel;
    private JTextField bookTitleField;
    private JTextField bookAuthorField;
    private JTextField bookGenreField;
    
    // Users tab
    private JTable usersTable;
    private DefaultTableModel usersModel;
    
    // Loans tab
    private JTable loansTable;
    private DefaultTableModel loansModel;
    
    private MainFrame parentFrame;
    
    /**
     * Create admin panel
     * @param parent Parent frame
     */
    public AdminPanel(MainFrame parent) {
        this.parentFrame = parent;
        initComponents();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Check if current user is admin
        if (!SessionManager.isAdmin()) {
            add(new JLabel("Нямате достъп до административния панел", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Административен панел", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(24.0f));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showDashboard();
            }
        });
        titlePanel.add(backButton, BorderLayout.EAST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Books tab
        JPanel booksPanel = new JPanel(new BorderLayout());
        
        // Book entry form
        JPanel bookFormPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        bookFormPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        bookFormPanel.add(new JLabel("Заглавие:"));
        bookTitleField = new JTextField(20);
        bookFormPanel.add(bookTitleField);
        
        bookFormPanel.add(new JLabel("Автор:"));
        bookAuthorField = new JTextField(20);
        bookFormPanel.add(bookAuthorField);
        
        bookFormPanel.add(new JLabel("Жанр:"));
        bookGenreField = new JTextField(20);
        bookFormPanel.add(bookGenreField);
        
        JButton addBookButton = new JButton("Добави книга");
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
        bookFormPanel.add(new JLabel(""));
        bookFormPanel.add(addBookButton);
        
        booksPanel.add(bookFormPanel, BorderLayout.NORTH);
        
        // Books table
        String[] bookColumns = {"ID", "Заглавие", "Автор", "Жанр", "Статус", "Добавена на"};
        booksModel = new DefaultTableModel(bookColumns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        booksTable = new JTable(booksModel);
        booksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane booksScrollPane = new JScrollPane(booksTable);
        booksPanel.add(booksScrollPane, BorderLayout.CENTER);
        
        // Books actions panel
        JPanel booksActionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton deleteBookButton = new JButton("Изтрий книга");
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });
        booksActionsPanel.add(deleteBookButton);
        
        booksPanel.add(booksActionsPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Управление на книги", booksPanel);
        
        // Users tab
        JPanel usersPanel = new JPanel(new BorderLayout());
        
        // Users table
        String[] userColumns = {"ID", "Име", "Имейл", "Роля", "Регистриран на"};
        usersModel = new DefaultTableModel(userColumns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        usersTable = new JTable(usersModel);
        usersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane usersScrollPane = new JScrollPane(usersTable);
        usersPanel.add(usersScrollPane, BorderLayout.CENTER);
        
        // User actions panel
        JPanel userActionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton changeRoleButton = new JButton("Промяна на роля");
        changeRoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeUserRole();
            }
        });
        userActionsPanel.add(changeRoleButton);
        
        JButton deleteUserButton = new JButton("Изтрий потребител");
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        userActionsPanel.add(deleteUserButton);
        
        usersPanel.add(userActionsPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Управление на потребители", usersPanel);
        
        // Loans tab
        JPanel loansPanel = new JPanel(new BorderLayout());
        
        // Loans table
        String[] loanColumns = {"ID", "Книга", "Потребител", "Дата на заемане", "Дата на връщане", "Статус"};
        loansModel = new DefaultTableModel(loanColumns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        loansTable = new JTable(loansModel);
        loansTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane loansScrollPane = new JScrollPane(loansTable);
        loansPanel.add(loansScrollPane, BorderLayout.CENTER);
        
        // Loan actions panel
        JPanel loanActionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton returnLoanButton = new JButton("Върни книга");
        returnLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnLoan();
            }
        });
        loanActionsPanel.add(returnLoanButton);
        
        loansPanel.add(loanActionsPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Управление на заемания", loansPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Add new book
     */
    private void addBook() {
        String title = bookTitleField.getText().trim();
        String author = bookAuthorField.getText().trim();
        String genre = bookGenreField.getText().trim();
        
        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Заглавието и авторът са задължителни полета", 
                    "Грешка", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setAvailability("налична");
        
        BookDAO bookDAO = new BookDAO();
        boolean success = bookDAO.addBook(book);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                    "Книгата е успешно добавена", 
                    "Успех", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form
            bookTitleField.setText("");
            bookAuthorField.setText("");
            bookGenreField.setText("");
            
            // Refresh books table
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Възникна грешка при добавянето на книгата", 
                    "Грешка", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete selected book
     */
    private void deleteBook() {
        int row = booksTable.getSelectedRow();
        
        if (row < 0) {
            JOptionPane.showMessageDialog(this, 
                    "Моля, изберете книга за изтриване", 
                    "Информация", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int bookId = (int) booksTable.getValueAt(row, 0);
        String bookTitle = (String) booksTable.getValueAt(row, 1);
        
        int result = JOptionPane.showConfirmDialog(this, 
                "Наистина ли искате да изтриете книгата \"" + bookTitle + "\"?", 
                "Потвърждение", 
                JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            BookDAO bookDAO = new BookDAO();
            boolean success = bookDAO.deleteBook(bookId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                        "Книгата е успешно изтрита", 
                        "Успех", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh books table
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Възникна грешка при изтриването на книгата. " +
                        "Възможно е книгата да има активни заемания.", 
                        "Грешка", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Change user role
     */
    private void changeUserRole() {
        int row = usersTable.getSelectedRow();
        
        if (row < 0) {
            JOptionPane.showMessageDialog(this, 
                    "Моля, изберете потребител", 
                    "Информация", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int userId = (int) usersTable.getValueAt(row, 0);
        String userName = (String) usersTable.getValueAt(row, 1);
        String currentRole = (String) usersTable.getValueAt(row, 3);
        
        String newRole = currentRole.equals("администратор") ? "потребител" : "администратор";
        
        int result = JOptionPane.showConfirmDialog(this, 
                "Искате ли да промените ролята на \"" + userName + "\" от \"" + 
                currentRole + "\" на \"" + newRole + "\"?", 
                "Потвърждение", 
                JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);
            
            if (user != null) {
                user.setRole(newRole);
                boolean success = userDAO.updateUser(user);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                            "Ролята е успешно променена", 
                            "Успех", 
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh users table
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Възникна грешка при промяната на ролята", 
                            "Грешка", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    /**
     * Delete selected user
     */
    private void deleteUser() {
        int row = usersTable.getSelectedRow();
        
        if (row < 0) {
            JOptionPane.showMessageDialog(this, 
                    "Моля, изберете потребител за изтриване", 
                    "Информация", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int userId = (int) usersTable.getValueAt(row, 0);
        String userName = (String) usersTable.getValueAt(row, 1);
        
        // Prevent deletion of current user
        if (userId == SessionManager.getCurrentUser().getUserId()) {
            JOptionPane.showMessageDialog(this, 
                    "Не можете да изтриете текущия потребител", 
                    "Грешка", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
                "Наистина ли искате да изтриете потребителя \"" + userName + "\"?", 
                "Потвърждение", 
                JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.deleteUser(userId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                        "Потребителят е успешно изтрит", 
                        "Успех", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh users table
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Възникна грешка при изтриването на потребителя. " +
                        "Възможно е потребителят да има активни заемания.", 
                        "Грешка", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Return selected loan
     */
    private void returnLoan() {
        int row = loansTable.getSelectedRow();
        
        if (row < 0) {
            JOptionPane.showMessageDialog(this, 
                    "Моля, изберете заемане за връщане", 
                    "Информация", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String status = (String) loansTable.getValueAt(row, 5);
        
        if (!status.equals("Активна") && !status.equals("Просрочена")) {
            JOptionPane.showMessageDialog(this, 
                    "Книгата вече е върната", 
                    "Информация", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int loanId = (int) loansTable.getValueAt(row, 0);
        String bookTitle = (String) loansTable.getValueAt(row, 1);
        String userName = (String) loansTable.getValueAt(row, 2);
        
        int result = JOptionPane.showConfirmDialog(this, 
                "Искате ли да отбележите, че книгата \"" + bookTitle + "\", заета от \"" + 
                userName + "\" е върната?", 
                "Потвърждение", 
                JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            LoanDAO loanDAO = new LoanDAO();
            boolean success = loanDAO.returnBook(loanId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                        "Книгата е успешно отбелязана като върната", 
                        "Успех", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh loans table
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Възникна грешка при връщането на книгата", 
                        "Грешка", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Refresh panel data
     */
    public void refreshData() {
        if (!SessionManager.isAdmin()) {
            return;
        }
        
        // Clear tables
        booksModel.setRowCount(0);
        usersModel.setRowCount(0);
        loansModel.setRowCount(0);
        
        // Load books
        BookDAO bookDAO = new BookDAO();
        List<Book> books = bookDAO.getAllBooks();
        
        for (Book book : books) {
            booksModel.addRow(new Object[] {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getAvailability(),
                book.getAddedAt()
            });
        }
        
        // Load users
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        
        for (User user : users) {
            usersModel.addRow(new Object[] {
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
            });
        }
        
        // Load loans
        LoanDAO loanDAO = new LoanDAO();
        List<Loan> loans = loanDAO.getAllLoans(true);
        
        for (Loan loan : loans) {
            String status = loan.getActualReturnDate() == null ? 
                    (isOverdue(loan.getReturnDate()) ? "Просрочена" : "Активна") : 
                    "Върната";
            
            loansModel.addRow(new Object[] {
                loan.getLoanId(),
                loan.getBookTitle(),
                loan.getUserName(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                status
            });
        }
    }
    
    /**
     * Check if loan is overdue
     * @param returnDateStr Return date string
     * @return true if loan is overdue, false otherwise
     */
    private boolean isOverdue(String returnDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date returnDate = sdf.parse(returnDateStr);
            Date currentDate = new Date();
            
            return currentDate.after(returnDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}