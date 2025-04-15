package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import dao.LoanDAO;
import models.Book;
import models.Loan;
import utils.SessionManager;

/**
 * Dashboard panel for main application view
 */
public class DashboardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JLabel welcomeLabel;
    private JTable recentBooksTable;
    private JTable activeLoansTable;
    private DefaultTableModel recentBooksModel;
    private DefaultTableModel activeLoansModel;
    
    private MainFrame parentFrame;
    
    /**
     * Create dashboard panel
     * @param parent Parent frame
     */
    public DashboardPanel(MainFrame parent) {
        this.parentFrame = parent;
        initComponents();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Welcome panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Добре дошли в библиотечната система!", SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(24.0f));
        
        JButton logoutButton = new JButton("Изход");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SessionManager.logout();
                parentFrame.showLoginPanel();
            }
        });
        
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.add(logoutButton, BorderLayout.EAST);
        
        add(welcomePanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Recent books panel
        JPanel recentBooksPanel = new JPanel(new BorderLayout());
        recentBooksPanel.setBorder(BorderFactory.createTitledBorder("Последно добавени книги"));
        
        String[] bookColumns = {"ID", "Заглавие", "Автор", "Жанр", "Статус"};
        recentBooksModel = new DefaultTableModel(bookColumns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        recentBooksTable = new JTable(recentBooksModel);
        recentBooksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        recentBooksTable.getColumnModel().getColumn(0).setMaxWidth(50);
        
        JScrollPane booksScrollPane = new JScrollPane(recentBooksTable);
        recentBooksPanel.add(booksScrollPane, BorderLayout.CENTER);
        
        JButton searchBooksButton = new JButton("Търсене на книги");
        searchBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showSearchPanel();
            }
        });
        recentBooksPanel.add(searchBooksButton, BorderLayout.SOUTH);
        
        contentPanel.add(recentBooksPanel);
        
        // Active loans panel
        JPanel activeLoansPanel = new JPanel(new BorderLayout());
        activeLoansPanel.setBorder(BorderFactory.createTitledBorder("Активни заемания"));
        
        String[] loanColumns = {"ID", "Книга", "Дата на заемане", "Дата на връщане"};
        activeLoansModel = new DefaultTableModel(loanColumns, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        activeLoansTable = new JTable(activeLoansModel);
        activeLoansTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        activeLoansTable.getColumnModel().getColumn(0).setMaxWidth(50);
        
        JScrollPane loansScrollPane = new JScrollPane(activeLoansTable);
        activeLoansPanel.add(loansScrollPane, BorderLayout.CENTER);
        
        JButton viewLoansButton = new JButton("Преглед на всички заемания");
        viewLoansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showLoanPanel();
            }
        });
        activeLoansPanel.add(viewLoansButton, BorderLayout.SOUTH);
        
        contentPanel.add(activeLoansPanel);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Admin panel
        if (SessionManager.isAdmin()) {
            JPanel adminActionsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            adminActionsPanel.setBorder(BorderFactory.createTitledBorder("Администраторски действия"));
            
            JButton addBookButton = new JButton("Добавяне на книга");
            addBookButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parentFrame.showAdminPanel();
                }
            });
            adminActionsPanel.add(addBookButton);
            
            JButton manageUsersButton = new JButton("Управление на потребители");
            manageUsersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parentFrame.showAdminPanel();
                }
            });
            adminActionsPanel.add(manageUsersButton);
            
            JButton manageLoansButton = new JButton("Управление на заемания");
            manageLoansButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parentFrame.showAdminPanel();
                }
            });
            adminActionsPanel.add(manageLoansButton);
            
            add(adminActionsPanel, BorderLayout.SOUTH);
        }
    }
    
    /**
     * Refresh panel data
     */
    public void refreshData() {
        if (!SessionManager.isLoggedIn()) {
            return;
        }
        
        // Update welcome message
        welcomeLabel.setText("Добре дошли, " + SessionManager.getCurrentUser().getName() + "!");
        
        // Clear tables
        recentBooksModel.setRowCount(0);
        activeLoansModel.setRowCount(0);
        
        // Load recent books
        BookDAO bookDAO = new BookDAO();
        List<Book> allBooks = bookDAO.getAllBooks();
        
        // Only show up to 5 most recent books
        int count = Math.min(allBooks.size(), 5);
        for (int i = 0; i < count; i++) {
            Book book = allBooks.get(i);
            recentBooksModel.addRow(new Object[] {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getAvailability()
            });
        }
        
        // Load active loans for current user
        LoanDAO loanDAO = new LoanDAO();
        List<Loan> activeLoans = loanDAO.getLoansByUserId(SessionManager.getCurrentUser().getUserId(), true);
        
        for (Loan loan : activeLoans) {
            activeLoansModel.addRow(new Object[] {
                loan.getLoanId(),
                loan.getBookTitle(),
                loan.getLoanDate(),
                loan.getReturnDate()
            });
        }
    }
}