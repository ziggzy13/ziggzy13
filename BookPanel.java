package ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import dao.LoanDAO;
import models.Book;
import models.Loan;
import utils.SessionManager;

import java.util.List;

/**
 * Book panel for displaying book details
 */
public class BookPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField titleField;
    private JTextField authorField;
    private JTextField genreField;
    private JTextField statusField;
    private JTextArea descriptionArea;
    private JTable loanHistoryTable;
    private DefaultTableModel loanHistoryModel;
    
    private JButton loanButton;
    private JButton editButton;
    
    private MainFrame parentFrame;
    private Book currentBook;
    
    /**
     * Create book panel
     * @param parent Parent frame
     */
    public BookPanel(MainFrame parent) {
        this.parentFrame = parent;
        initComponents();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Информация за книгата", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(24.0f));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showSearchPanel();
            }
        });
        titlePanel.add(backButton, BorderLayout.EAST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Book details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Детайли за книгата"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Заглавие:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        titleField = new JTextField(20);
        titleField.setEditable(false);
        detailsPanel.add(titleField, gbc);
        
        // Author
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Автор:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        authorField = new JTextField(20);
        authorField.setEditable(false);
        detailsPanel.add(authorField, gbc);
        
        // Genre
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Жанр:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        genreField = new JTextField(20);
        genreField.setEditable(false);
        detailsPanel.add(genreField, gbc);
        
        // Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Статус:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        statusField = new JTextField(20);
        statusField.setEditable(false);
        detailsPanel.add(statusField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Описание:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        detailsPanel.add(descriptionScrollPane, gbc);
        
        // Buttons panel
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonsPanel = new JPanel();
        
        loanButton = new JButton("Заеми книгата");
        loanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loanBook();
            }
        });
        buttonsPanel.add(loanButton);
        
        editButton = new JButton("Редактирай");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBook();
            }
        });
        editButton.setVisible(SessionManager.isAdmin());
        buttonsPanel.add(editButton);
        
        detailsPanel.add(buttonsPanel, gbc);
        
        add(detailsPanel, BorderLayout.CENTER);
        
        // Loan history panel (only visible for admins)
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("История на заеманията"));
        
        String[] columns = {"ID", "Потребител", "Дата на заемане", "Дата на връщане", "Действителна дата на връщане"};
        loanHistoryModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        loanHistoryTable = new JTable(loanHistoryModel);
        loanHistoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane historyScrollPane = new JScrollPane(loanHistoryTable);
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);
        
        historyPanel.setVisible(SessionManager.isAdmin());
        
        add(historyPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Load book data
     * @param bookId Book ID
     */
    public void loadBook(int bookId) {
        BookDAO bookDAO = new BookDAO();
        currentBook = bookDAO.getBookById(bookId);
        
        if (currentBook != null) {
            titleField.setText(currentBook.getTitle());
            authorField.setText(currentBook.getAuthor());
            genreField.setText(currentBook.getGenre());
            statusField.setText(currentBook.getAvailability());
            
            // Enable/disable loan button based on availability
            loanButton.setEnabled(currentBook.isAvailable());
            
            // Clear loan history
            loanHistoryModel.setRowCount(0);
            
            // Load loan history if admin
            if (SessionManager.isAdmin()) {
                LoanDAO loanDAO = new LoanDAO();
                List<Loan> loanHistory = loanDAO.getAllLoans(false);
                
                for (Loan loan : loanHistory) {
                    if (loan.getBookId() == bookId) {
                        loanHistoryModel.addRow(new Object[] {
                            loan.getLoanId(),
                            loan.getUserName(),
                            loan.getLoanDate(),
                            loan.getReturnDate(),
                            loan.getActualReturnDate() != null ? loan.getActualReturnDate() : "Не е върната"
                        });
                    }
                }
            }
        }
    }
    
    /**
     * Loan current book
     */
    private void loanBook() {
        if (currentBook == null || !currentBook.isAvailable()) {
            JOptionPane.showMessageDialog(this, 
                    "Книгата не е налична за заемане", 
                    "Грешка", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
                "Искате ли да заемете книгата \"" + currentBook.getTitle() + "\"?", 
                "Потвърждение", 
                JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            LoanDAO loanDAO = new LoanDAO();
            boolean success = loanDAO.createLoan(currentBook.getBookId(), SessionManager.getCurrentUser().getUserId());
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                        "Книгата е успешно заета", 
                        "Успех", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh book data
                loadBook(currentBook.getBookId());
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Възникна грешка при заемането на книгата", 
                        "Грешка", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Edit current book (admin only)
     */
    private void editBook() {
        if (!SessionManager.isAdmin() || currentBook == null) {
            return;
        }
        
        // Show edit dialog
        // In a real application, this would open a dialog to edit the book details
        JOptionPane.showMessageDialog(this, 
                "Функционалността за редактиране на книга не е имплементирана в това демо", 
                "Информация", 
                JOptionPane.INFORMATION_MESSAGE);
    }
}